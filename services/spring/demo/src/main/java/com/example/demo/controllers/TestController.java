package com.example.demo.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;

@RestController
public class TestController {
   
   @Autowired
   private MongoTemplate mongoTemplate;

   private final ObjectMapper objectMapper = new ObjectMapper();

   @GetMapping("/hello")
   public List<Document> jsonRetrieve() {
      // return "Hello, Spring Boot!";
      // return mongoTemplate.findAll(
      //    JsonNode.class,
      //    "dynamic_collection"
      // );

      List<Document> documents = mongoTemplate.findAll(Document.class, "dynamic_collection");

      for (Document document : documents) {
         System.out.println("===GK > " + document);
      }

      return documents;
      // return documents.stream()
      //    .map(document -> objectMapper.valueToTree(document))
      //    .collect(Collectors.toList());

   }

   @PostMapping("/hello")
   public ResponseEntity<String> jsonStore(
      @RequestBody
      // JsonNode jsonNode
      // Map<String, Object> jsonMap
      Document jsonMap
   ) {

      // for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
      //    System.out.println("===GK > " + entry.getKey() + ": " + entry.getValue());
      // }
      
      mongoTemplate.save(jsonMap, "dynamic_collection");
      return ResponseEntity.ok("Processed the Map");
      
      // return ResponseEntity.ok("JSON object stored successfully.");
      // mongoTemplate.save(jsonMap, "dynamic_collection");

      // String response = "Received: [" + body + "]";
      // System.out.println(response);
      // return response;
   }

}
