package com.example.demo.models;

import org.bson.Document;
import org.springframework.data.annotation.Id;

public class MongoDocument extends Document{

   @Id
   private String id;
}
