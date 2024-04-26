package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DataModel;
import com.example.demo.repositories.DataRepository;



@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/data")
public class DataController {

   private static final String collection_name = "data";

   @Autowired
   DataRepository dataRepository;

   @Autowired
   private MongoTemplate mongoTemplate;

   @GetMapping("")
   // RETRIEVE
   public ResponseEntity<List<Document>> getAllData(
      @RequestParam(required = false)
      String query
   ){
      // TODO: make use of the query parameter
      
      // TODO: Implement pagination

      // Retrieve all documents
      List<Document> documents = mongoTemplate.findAll(
         Document.class, 
         collection_name
      );

      // Return a response with all the documents
      return new ResponseEntity<>(documents,HttpStatus.OK);
   }

   @GetMapping("/{id}")
   // RETRIEVE
   public ResponseEntity<Document> getData(
      @PathVariable("id")
      String id
   ){
      Document result = mongoTemplate.findById(
         id, 
         Document.class,
         collection_name
      );

      System.out.println("Found: "+result);
      if (result != null)
         return new ResponseEntity<>(result, HttpStatus.OK);
      else
         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
   }

   @PostMapping("")
   // CREATE
   public ResponseEntity<Document> createData(
      @RequestBody
      Document document
   ) {
      Document saved_document = mongoTemplate.save(document, collection_name);
      return new ResponseEntity<>(saved_document, HttpStatus.OK);
   }

   @PutMapping("/{id}")
   // UPDATE
   public ResponseEntity<Document> updateData(
      @PathVariable
      String id,
      @RequestBody
      Document new_document
   ) {

      Document found_document = mongoTemplate.findById(
         id, 
         Document.class,
         collection_name
      );

      if (found_document == null)
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      
      Document saved_document = mongoTemplate.save(new_document, collection_name);
      return new ResponseEntity<>(saved_document, HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   // DELETE
   public ResponseEntity<HttpStatus> deleteData(
      @PathVariable("id")
      String id
   ) {
      mongoTemplate.remove(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping("")
   // DELETE
   public ResponseEntity<HttpStatus> deleteAllData() {
      mongoTemplate.remove(new Query());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

}
