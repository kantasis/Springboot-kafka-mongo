package com.example.demo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.catalina.connector.Response;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DataModel;
import com.example.demo.repositories.DataRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/data")
@Tag(name = "Data API", description = "Manage JSON documents")
@Log4j2
public class DataController {

   // private static final String collection_name = "data";

   @Autowired
   DataRepository dataRepository;

   @Autowired
   private MongoTemplate mongoTemplate;

   @GetMapping("/{collection}")
   @Operation(
      summary = "Query the collection",
      description = "Get the documents in the specified collection. Use the body of the request to specify the query"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Retrieval of all the documents", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "204", description = "No documents to retrieve", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
      // @ApiResponse(responseCode = "404", description = "No documents to retrieve", content = { @Content(schema = @Schema()) }),
      // @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<List<Document>> getAllData(
      
      @PathVariable("collection")
      String collection_name,

      @RequestBody(required=false)
      String query_str

   ){
      // TODO: Implement pagination

      log.info("GK> Got query!: " + query_str);
      
      BasicQuery basicQuery = new BasicQuery(query_str);
      List<Document> documents = mongoTemplate.find( 
         basicQuery,
         Document.class,
         collection_name
      );

      if (documents.isEmpty())
         return new ResponseEntity<>(
            documents,
            HttpStatus.NO_CONTENT
         );
         
      return new ResponseEntity<>(
         documents,
         HttpStatus.OK
      );

   }


   @GetMapping("/")
   @Operation(
      summary = "List collections",
      description = "Lists the available collections in the data lake"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "List of collections retrieved", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "202", description = "No collections are in the data lake", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<Set<String>> getCollections(){

      try {
         // Retrieve the list of all collection names
         Set<String> collections_StrSet = mongoTemplate.getCollectionNames();

         if (collections_StrSet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }

         return new ResponseEntity<>(collections_StrSet, HttpStatus.OK);

      } catch (Exception e) {
         log.error("Error while retrieving collections", e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }

   }

   @GetMapping("/{collection}/{id}")
   @Operation(
      summary = "Retrieve a specific document",
      description = "Find a document by ID form the specified collection."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Document.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<Document> getData(
      @PathVariable("collection")
      String collection_name,
      @PathVariable("id")
      String id
   ){
      Document result_Document = mongoTemplate.find(
         new BasicQuery("{\"id\": \""+id+"\"}"),
         Document.class,
         collection_name
      )
      .get(0); // We assume ids are unique

      // TODO: Make this more pretty
      if (result_Document != null)
         return new ResponseEntity<>(result_Document, HttpStatus.OK);
      else
         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
   }

   @PostMapping("/{collection}")
   @Operation(
      summary = "Create a new document in the collection",
      description = "Creates a new document from the specified body"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Document.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<Document> createData(
      @PathVariable("collection")
      String collection_name,
      @RequestBody
      Document document
   ) {
      try{
         log.info("GK> Received a POST request");
         
         if (! document.containsKey("id") ){
            log.info("GK> Received a POST request without an ID field");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
         
         String id = document.getString("id");

         int found_cnt = mongoTemplate.find(
            new BasicQuery("{\"id\": \""+id+"\"}"),
            Document.class,
            collection_name
         ).size();

         if (found_cnt>0){
            log.info("GK> Received a POST request with an existing ID, replacing");
         }

         Document saved_Document = mongoTemplate.save(document, collection_name);
         return new ResponseEntity<>(saved_Document, HttpStatus.OK);
      } catch (Exception e) {
         log.error("Error while posting the document: "+ e.getMessage(), e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @PutMapping("/{collection}/{id}")
   @Operation(
      summary = "Update an existing document",
      description = "Finds an existing document by ID and replaces it with the document in the request"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Document.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<Document> putData(
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
   
      Document saved_Document = mongoTemplate.save(new_document, collection_name);
      return new ResponseEntity<>(saved_Document, HttpStatus.OK);
   }
   
   @PatchMapping("/{collection}/{id}")
   @Operation(
      summary = "Update specific fields of a document",
      description = "Finds an existing document by ID and updates its fields according to the request body"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Document successfully updated", content = { @Content(schema = @Schema(implementation = Document.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", description = "Document not found", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "400", description = "Invalid request body or query", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
   public ResponseEntity<Document> patchData(

      @PathVariable("collection")
      String collection_name,
      
      @PathVariable
      String id,
      
      @RequestBody 
      Map<String, Object> changes_map
   
   ) {
      try {

         // String new_id = new_document.getString("id");
         // BasicQuery idQuery = new BasicQuery("{\"id\": \""+id+"\"}");
         Query query = new Query(Criteria.where("id").is(id));

         Update update = new Update();

         for (Map.Entry<String, Object> entry : changes_map.entrySet()) {
            String key_str = entry.getKey();
            String val_str = entry.getValue().toString();
            log.info("GK> "+key_str+" - " + val_str);
            update.set(entry.getKey(), entry.getValue());
         }

         Document new_doc = mongoTemplate.findAndModify(
            query, 
            update, 
            FindAndModifyOptions.options().returnNew(true), 
            Document.class, 
            collection_name
         );
   
         if (new_doc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         
         return new ResponseEntity<>(new_doc, HttpStatus.OK);

      } catch (Exception e) {
         log.error("Error while patching the document", e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }
   
   @DeleteMapping("/{collection}/{id}")
   @Operation(
      summary = "Delete a document",
      description = "Find a document by its ID in the specified collection and remove it"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = HttpStatus.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
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
   @Operation(
      summary = "Delete a collection",
      description = "Remove all documents from the specified collection"
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = HttpStatus.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
   })
   @SecurityRequirement(name = "Authorization")
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
