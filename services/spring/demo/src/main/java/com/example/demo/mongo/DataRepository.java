package com.example.demo.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepository extends MongoRepository <
   DataModel,  // Class of the data
   String      // Class of the ID
>{

}
