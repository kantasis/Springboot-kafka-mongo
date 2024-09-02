package com.example.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.models.DataModel;
import com.example.demo.repositories.DataRepository;

@Component
public class MessageConsumer {

      // @Autowired
      // DataRepository dataRepository;

      // @KafkaListener(topics="my-topic", groupId="my-group-id")
      // public void listen(String message){
      //    System.out.println("Received message: " + message);
         
      //    DataModel dataModel = new DataModel(message);
      //    dataRepository.insert(dataModel);
      // }

}
