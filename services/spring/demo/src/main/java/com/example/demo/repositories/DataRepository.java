package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.models.DataModel;

public interface DataRepository extends MongoRepository<DataModel,String> {
   Optional<DataModel> findById(String id);
}
