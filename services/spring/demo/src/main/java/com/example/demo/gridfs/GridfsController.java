package com.example.demo.gridfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin("*")
@RequestMapping("api/")
@Tag(name = "File API", description = "Manage large binary files")
public class GridfsController {

   @Autowired
   private GridfsService gridfsService;

   @PostMapping("files")
   @Operation(
      summary = "Upload a file",
      description = "Store a multipart file to the data lake. Returns the ID of the file stored."
   )
   @ApiResponses({
      @ApiResponse(
         responseCode = "200",
         description = "Successful response",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
   })
   public ResponseEntity<String> upload(
      @RequestParam("file")
      MultipartFile file
   ) throws IOException {
      String storedID_str = gridfsService.saveFile(file);
      return new ResponseEntity<String>(storedID_str, HttpStatus.OK);
   }

   @GetMapping("files/{id}")
   @Operation(
      summary = "Download a file",
      description = "Gets the file that matches the ID param."
   )
   @ApiResponses({
      @ApiResponse(
         responseCode = "200",
         description = "Successful response",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
      @ApiResponse(
         responseCode = "404",
         description = "ID not found in the data lake",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
   })
   public ResponseEntity<ByteArrayResource> download(
      @PathVariable 
      String id
   ) throws IOException {

      GridfsModel gridfsModel = gridfsService.loadFile(id);

      if (gridfsModel == null)
         return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            // .notFound()
            .body(null);

      return ResponseEntity
         .ok()
         .contentType(
            MediaType.parseMediaType(
               gridfsModel.getFileType() 
            )
         )
         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gridfsModel.getFilename() + "\"")
         .body(new ByteArrayResource(gridfsModel.getBytes()));
   }

   @GetMapping("files")
   @Operation(
      summary = "List the files in the data lake",
      description = "Gets the file that matches the ID param."
   )
   @ApiResponses({
      @ApiResponse(
         responseCode = "200",
         description = "Successful response",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
      @ApiResponse(
         responseCode = "404",
         description = "ID not found in the data lake",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
   })
   public ResponseEntity<Map<String, String>> list() throws IOException {
      
      Map<String, String> files_hashMap = gridfsService.listAllFiles();

      return ResponseEntity
         .ok()
         .body(files_hashMap);
   }

   @DeleteMapping("files/{id}")
   @Operation(
      summary = "Delete a file",
      description = "Delete the file with the specified ID."
   )
   @ApiResponses({
      @ApiResponse(
         responseCode = "200",
         description = "Successful response",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
      @ApiResponse(
         responseCode = "204",
         description = "ID not found in the data lake",
         content = { 
            @Content(
               schema = @Schema(implementation = ResponseEntity.class), 
               mediaType = "application/json"
            )
         }
      ),
   })
   public ResponseEntity<String> delete(
      @PathVariable 
      String id
   ) throws IOException {
      
      boolean success_bool = gridfsService.delete(id);

      if (!success_bool)
         return new ResponseEntity<>(
            "ID not found",
            HttpStatus.NO_CONTENT
         );

      return new ResponseEntity<>(
         "Deleted",
         HttpStatus.OK
      );
   }

}
