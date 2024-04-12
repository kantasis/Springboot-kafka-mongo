package com.example.demo.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Annotation to signify this is a mongodb document
@Document
public class DataModel {
   
   private String _data_str;

   // Annotation to signify this variable is the id of the document
   @Id
   private String _id_l;

   public String get_data() {
      return _data_str;
   }
   public void set_data(String data_str) {
      this._data_str = data_str;
   }
   public String get_id() {
      return _id_l;
   }
   public void set_id(String id) {
      this._id_l = id;
   }
   
   public DataModel() {
   }

   public DataModel(String data_str) {
      this._data_str = data_str;
   }

   @Override
   public String toString() {
      return "DataModel (" + get_data() + "): " + get_data();
   }

}
