package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="roles")
public class RoleModel {
   @Id
   private String id;
   private RoleEnum role_enum;



   public RoleModel(RoleEnum role_enum) {
      this.role_enum = role_enum;
   }

   public RoleModel() {}

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public RoleEnum getRoleEnum() {
      return role_enum;
   }
   public void setRoleEnum(RoleEnum role_enum) {
      this.role_enum = role_enum;
   }

}
