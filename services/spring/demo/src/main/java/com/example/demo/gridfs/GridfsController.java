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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@CrossOrigin("*")
@RequestMapping("api/")
public class GridfsController {

   @Autowired
   private GridfsService gridfsService;

   @PostMapping("upload")
   public ResponseEntity<?> upload(
      @RequestParam("file")
      MultipartFile file
   ) throws IOException {
      String storedID_str = gridfsService.saveFile(file);
      return new ResponseEntity<>(storedID_str, HttpStatus.OK);
   }

   @GetMapping("download/{id}")
   public ResponseEntity<ByteArrayResource> download(
      @PathVariable 
      String id
   ) throws IOException {

      GridfsModel gridfsModel = gridfsService.loadFile(id);

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

   @GetMapping("list")
   public ResponseEntity<Map<String, String>> list() throws IOException {
      
      Map<String, String> files_hashMap = gridfsService.listAllFiles();

      return ResponseEntity
         .ok()
         .body(files_hashMap);
   }

   @DeleteMapping("delete/{id}")
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
