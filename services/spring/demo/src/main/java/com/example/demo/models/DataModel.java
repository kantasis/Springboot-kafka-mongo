package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Annotation to signify this is a mongodb document
@Document
public class DataModel {
   
   private String _data_str;

   // Annotation to signify this variable is the id of the document
   @Id
   private long _id_l;

   public String get_data() {
      return _data_str;
   }
   public void set_data(String _data_str) {
      this._data_str = _data_str;
   }
   public long get_id() {
      return _id_l;
   }
   public void set_id(long _id_l) {
      this._id_l = _id_l;
   }
   
   public DataModel() {
   }

   public DataModel(String _data_str, long _id_l) {
      this._data_str = _data_str;
      this._id_l = _id_l;
   }

}
