package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="data")
@NoArgsConstructor
@Data
public class DataModel {

   @Id
   private String id;

   private String data;

}
