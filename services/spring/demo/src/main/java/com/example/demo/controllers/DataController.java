package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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

   // private static final String collection_name = "data";

   @Autowired
   DataRepository dataRepository;

   @Autowired
   private MongoTemplate mongoTemplate;

   @GetMapping("/{collection}")
   // RETRIEVE
   public ResponseEntity<List<Document>> getAllData(
      @PathVariable("collection")
      String collection_name,
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

   @GetMapping("/{collection}/{id}")
   // RETRIEVE
   public ResponseEntity<Document> getData(
      @PathVariable("collection")
      String collection_name,
      @PathVariable("id")
      String id
   ){
      Document result = mongoTemplate.find(
         new BasicQuery("{\"id\": \""+id+"\"}"),
         Document.class,
         collection_name
      )
      // We assume ids are unique
      .get(0);

      // TODO: Make this more pretty
      if (result != null)
         return new ResponseEntity<>(result, HttpStatus.OK);
      else
         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
   }

   @PostMapping("/{collection}")
   // CREATE
   public ResponseEntity<Document> createData(
      @PathVariable("collection")
      String collection_name,
      @RequestBody
      Document document
   ) {
      
      if (! document.containsKey("id") ){
         System.out.println("GK> Received a POST request without an ID field");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      
      String id = document.getString("id");

      int found_cnt = mongoTemplate.find(
         new BasicQuery("{\"id\": \""+id+"\"}"),
         Document.class,
         collection_name
      ).size();

      if (found_cnt>0){
         System.out.println("GK> Received a POST request with an existing ID");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

      Document saved_document = mongoTemplate.save(document, collection_name);
      return new ResponseEntity<>(saved_document, HttpStatus.OK);
   }

   @PutMapping("/{collection}/{id}")
   // UPDATE
   public ResponseEntity<Document> updateData(
      @PathVariable("collection")
      String collection_name,
      @PathVariable
      String id,
      @RequestBody
      Document new_document
   ) {

      if (! new_document.containsKey("id") ){
         System.out.println("GK> the new document should also contain the id field (PUT)");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      
      String new_id = new_document.getString("id");
      if ( ! new_id.equals(id) ){
         System.out.println("GK> id fields in request and body should match (PUT)");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

      List<Document> found_lst = mongoTemplate.find(
         new BasicQuery("{\"id\": \""+id+"\"}"),
         Document.class,
         collection_name
      );

      int found_cnt = found_lst.size();
      
      if (found_cnt == 0 ){
         System.out.println("GK> Document not found (PUT)");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

      if (found_cnt > 1){
         System.out.println("GK> Multiple documents with the same id exist (PUT) (for some reason)");
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

      mongoTemplate.remove(
         found_lst.get(0),
         collection_name
      );
   
      Document saved_document = mongoTemplate.save(new_document, collection_name);
      return new ResponseEntity<>(saved_document, HttpStatus.OK);
   }

   @DeleteMapping("/{collection}/{id}")
   // DELETE
   public ResponseEntity<HttpStatus> deleteData(
      @PathVariable("collection")
      String collection_name,
      @PathVariable("id")
      String id
   ) {
      String query = "{\"id\": \""+id+"\"}";
      System.out.println("Query: " + query);
      Document found_document = mongoTemplate.find(
         new BasicQuery(query),
         Document.class,
         collection_name
      ).get(0);

      // TODO: Return how many documents were removed
      mongoTemplate.remove(
         found_document,
         collection_name
      );
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping("/{collection}")
   // DELETE
   public ResponseEntity<HttpStatus> deleteAllData(
      @PathVariable("collection")
      String collection_name
   ) {
      mongoTemplate.remove(
         new BasicQuery("{}"),
         collection_name
      );
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

}
