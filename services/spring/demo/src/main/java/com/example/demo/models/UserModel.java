package com.example.demo.models;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// TODO: rename this to _users?
@Document(collection="users")
public class UserModel {

   @Id
   private String id;

   @NotBlank
   @Size(max=20)
   private String username;

   @NotBlank
   @Size(max=50)
   private String email;

   @NotBlank
   @Size(max=120)
   private String password;

   @DBRef
   private Set<RoleModel> roles = new HashSet<>();

   public UserModel() {
   }

   public UserModel(
      @NotBlank 
      @Size(max = 20) 
      String username, 

      @NotBlank 
      @Size(max = 50) 
      String email,

      @NotBlank 
      @Size(max = 120) 
      String password
   ) {
      this.username = username;
      this.email = email;
      this.password = password;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<RoleModel> getRoles() {
      return roles;
   }

   public void setRoles(Set<RoleModel> roles) {
      this.roles = roles;
   }

   
}
