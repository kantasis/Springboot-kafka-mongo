package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.jwt.AuthEntryPointJWT;
import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.services.UserDetailsServiceImpl;

@Configuration
// allows Spring to find and automatically apply the class to the global Web Security.
// @EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

   @Autowired
   // TODO: explicitly set this to private
   // TODO: Rename the var name to userDetailsServiceImpl
   UserDetailsServiceImpl userDetailsService;

   @Autowired
   private AuthEntryPointJWT unauthorizeHandler;
   
   @Bean
   // TODO: Rename this function to a verb
   public AuthTokenFilter authenticationJwtTokenFilter(){
      return new AuthTokenFilter();
   }
   
   @Bean
   // TODO: Rename this function to a verb
   public DaoAuthenticationProvider authenticationProvider(){
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService);
      // If we don’t specify, it will use plain text.
      authProvider.setPasswordEncoder(passwordEncoder());

      return authProvider;
   }

   @Bean
   // TODO: Rename this function to a verb
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
      return authConfig.getAuthenticationManager();
   }

   @Bean
   // TODO: Rename this function to a verb
   public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
   }

   @Bean
   // TODO: Rename this function to a verb
   public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
      httpSecurity
         .csrf(csrf -> csrf.disable())
         .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizeHandler))
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/content/**").permitAll()
            .anyRequest().authenticated()
            // .anyRequest().permitAll()
         );
      httpSecurity.authenticationProvider(authenticationProvider());
      httpSecurity.addFilterBefore(
         authenticationJwtTokenFilter(), 
         UsernamePasswordAuthenticationFilter.class
      );
      
      return httpSecurity.build();
   }
}
