package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DataModel;
import com.example.demo.repositories.DataRepository;



@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/data")
public class DataController {

   @Autowired
   DataRepository dataRepository;

   @GetMapping("")
   // RETRIEVE
   public ResponseEntity<List<DataModel>> getAllData(
      @RequestParam(required = false)
      String query
   ){
      // TODO: make use of the query parameter
      // System.out.println("getAllData");
      List<DataModel> data_lst = new ArrayList<DataModel>();
      data_lst = dataRepository.findAll();
      return new ResponseEntity<>(data_lst,HttpStatus.OK);
   }

   @GetMapping("/{id}")
   // RETRIEVE
   public ResponseEntity<DataModel> getData(
      @PathVariable("id")
      String id
   ){
      Optional<DataModel> dataModel = dataRepository.findById(id);
      if (dataModel.isPresent())
         return new ResponseEntity<>(dataModel.get(), HttpStatus.OK);
      else
         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
   }

   @PostMapping("")
   // CREATE
   public ResponseEntity<DataModel> createData(
      @RequestBody
      DataModel dataModel
   ) {
      // System.out.println("createData");
      try {
         DataModel _dataModel = dataRepository.save(dataModel);
         return new ResponseEntity<>(_dataModel,HttpStatus.OK);
      } catch (Exception e){
         return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @PutMapping("/{id}")
   // UPDATE
   public ResponseEntity<DataModel> updateData(
      @PathVariable
      String id,
      @RequestBody
      DataModel given_dataModel
   ) {
      Optional<DataModel> found_dataModel = dataRepository.findById(id);
      if (!found_dataModel.isPresent())
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      
      DataModel edited_dataModel = dataRepository.save(given_dataModel);
      return new ResponseEntity<>(edited_dataModel, HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   // DELETE
   public ResponseEntity<HttpStatus> deleteData(
      @PathVariable("id")
      String id
   ) {
      try {
         dataRepository.deleteById(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }catch(Exception e){
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @DeleteMapping("")
   // DELETE
   public ResponseEntity<HttpStatus> deleteAllData() {
      try {
         dataRepository.deleteAll();
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }catch(Exception e){
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

}
