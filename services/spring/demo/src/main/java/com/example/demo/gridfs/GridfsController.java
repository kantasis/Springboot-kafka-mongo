package com.example.demo.gridfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
         .contentType(MediaType.parseMediaType(gridfsModel.getFileType() ))
         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gridfsModel.getFilename() + "\"")
         .body(new ByteArrayResource(gridfsModel.getBytes()));
   }

   @GetMapping("list")
   public ResponseEntity<List<String>> list() throws IOException {
      List<String> files = gridfsService.listAllFiles();

      return ResponseEntity
         .ok()
         .body(files);
   }

   // TODO: Delete a file
}
