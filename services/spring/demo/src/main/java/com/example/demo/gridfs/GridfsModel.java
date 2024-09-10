package com.example.demo.gridfs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridfsModel {

   private String filename;
   private String fileType;
   private String fileSize;
   private byte[] bytes;

}
