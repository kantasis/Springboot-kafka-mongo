package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="roles")
public class RoleModel {

   @Id
   private String id;

   // This var name should be 'name' (???)
   private RoleEnum name;

   public RoleModel(RoleEnum name) {
      this.name = name;
   }

   public RoleModel() {}

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public RoleEnum getName() {
      return name;
   }
   public void setRoleEnum(RoleEnum name) {
      this.name = name;
   }

}
