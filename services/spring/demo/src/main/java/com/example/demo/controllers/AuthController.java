package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.RoleEnum;
import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.payloads.JwtResponse;
import com.example.demo.security.payloads.LoginRequest;
import com.example.demo.security.payloads.MessageResponse;
import com.example.demo.security.payloads.SignupRequest;
import com.example.demo.security.services.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
@Log4j2
@Tag(name = "Authentication API", description = "Signup and signin endpoints")
public class AuthController {

   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   UserRepository userRepository;

   @Autowired
   RoleRepository roleRepository;

   @Autowired
   PasswordEncoder passwordEncoder;

   @Autowired
   JwtUtils jwtUtils;

   @Operation(
      summary = "Login",
      description = "Provide a username and a password to receive a JWT"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) 
   })
   @PostMapping("/signin")
   public ResponseEntity<?> authenticateUser(
      @Valid
      @RequestBody
      LoginRequest loginRequest
   ){
      Authentication authentication = authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
         )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities()
         .stream()
         .map(item -> 
            item.getAuthority()
         )
         .collect(Collectors.toList());


      JwtResponse response = new JwtResponse(
         jwt,
         userDetails.getId(),
         userDetails.getUsername(),
         userDetails.getEmail(),
         roles
      );

      return ResponseEntity.ok(response);
   }

   @Operation(
      summary = "Register",
      description = "Provide a username, email and password to register in the API"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) 
   })
   @PostMapping("/signup")
   public ResponseEntity<?> registerUser (
      @Valid
      @RequestBody
      SignupRequest signupRequest
   ){
      log.info("GK> Processing a register request");
      if (userRepository.existsByUsername(signupRequest.getUsername()))
         return ResponseEntity
            .badRequest()
            .body( new MessageResponse("Error email is already in use"));

      UserModel user = new UserModel(
         signupRequest.getUsername(),
         signupRequest.getEmail(),
         passwordEncoder.encode(signupRequest.getPassword())
      );

      // TODO: Refactor this ugly thing
      Set<String> roles_strLst = signupRequest.getRoles();
      Set<RoleModel> roles = new HashSet<>();
      
      if (roles_strLst == null) {
         RoleModel roleModel = roleRepository
            .findByName(RoleEnum.ROLE_USER)
            .orElseThrow( () -> new RuntimeException("Error: Role is not found") );
         roles.add(roleModel);
      } else {
         roles_strLst.forEach(role_str -> {
            RoleEnum roleEnum;
            if ( role_str == "admin" )
               roleEnum = RoleEnum.ROLE_ADMIN;
            else if ( role_str == "mod" )
               roleEnum = RoleEnum.ROLE_MODERATOR;
            else
               // TODO: This one should not be the default case
               roleEnum = RoleEnum.ROLE_USER;

            RoleModel roleModel = roleRepository
               .findByName(roleEnum)
               .orElseThrow( () -> new RuntimeException("Error: Role is not found, did you initialize the database correctly?") );
            roles.add(roleModel);
         });
      }
      user.setRoles(roles);
      userRepository.save(user);
      return ResponseEntity.ok(
         new MessageResponse("User registered successfully")
      );
   }

   @GetMapping("/whoami")
   public ResponseEntity<?> whoami() {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      Object principal_obj = authentication.getPrincipal();
      String message_str; 
      if (principal_obj instanceof UserDetailsImpl) {
         message_str = ((UserDetailsImpl) principal_obj).getUsername();
      }else{
         message_str = principal_obj.toString();
      }

      log.info("You are: "+message_str);
      return ResponseEntity
         .ok()
         .body(
            new MessageResponse(message_str)
         )
      ;
   }

}
