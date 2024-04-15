package com.example.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/content")
public class ContentController {

   @GetMapping("/all")
   public String allAccess(){
      return "public content";
   }

   @GetMapping("/user")
   @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
   public String userAccess(){
      return "user access";
   }

   @GetMapping("/mod")
   @PreAuthorize("hasRole('MODERATOR')")
   public String modAccess(){
      return "moderator access";
   }

   @GetMapping("/admin")
   @PreAuthorize("hasRole('ADMIN')")
   public String adminAccess(){
      return "admin access";
   }
}
