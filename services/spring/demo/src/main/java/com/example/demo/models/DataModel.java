package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document(collection="data")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class DataModel {

   @Id
   private String id;

   private String data;

}
