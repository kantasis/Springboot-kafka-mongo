package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.models.RoleEnum;
import com.example.demo.models.RoleModel;

public interface RoleRepository extends MongoRepository <RoleModel,String> {
   Optional<RoleModel> findByName(RoleEnum name);
}
