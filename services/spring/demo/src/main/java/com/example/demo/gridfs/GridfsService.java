package com.example.demo.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GridfsService {

   @Autowired
   private GridFsTemplate gridFsTemplate;

   @Autowired
   private GridFsOperations operations;

   public String saveFile(MultipartFile uploaded_MultipartFile) throws IOException {

      DBObject metadata = new BasicDBObject();
      metadata.put("fileSize", uploaded_MultipartFile.getSize());

      Object fileID_obj = gridFsTemplate.store(
         uploaded_MultipartFile.getInputStream(), 
         uploaded_MultipartFile.getOriginalFilename(), 
         uploaded_MultipartFile.getContentType(), 
         metadata
      );

      return fileID_obj.toString();
   }

   public GridfsModel loadFile(String id) throws IOException {

      GridFSFile gridFSFile = gridFsTemplate.findOne( 
         new Query( Criteria
            .where("_id")
            .is(id)
         ) 
      );

      if (gridFSFile == null )
         return null;

      if (gridFSFile.getMetadata() == null )
         return null;

      return new GridfsModel(
         gridFSFile.getFilename(),
         gridFSFile.getMetadata().get("_contentType").toString(),
         gridFSFile.getMetadata().get("fileSize").toString(),
         org.apache.commons.io.IOUtils.toByteArray( operations.getResource(gridFSFile).getInputStream() )
      );
   }

   public List<String> listAllFiles() {
      // Create an empty query to fetch all files
      List<String> fileNames = new ArrayList<>();
      gridFsTemplate.find(new Query())
         .forEach(gridFSFile -> {
            fileNames.add(gridFSFile.getFilename());
         }
      );
      return fileNames;
   }


}
