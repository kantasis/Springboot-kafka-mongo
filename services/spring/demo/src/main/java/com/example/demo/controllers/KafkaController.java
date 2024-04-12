package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.kafka.MessageProducer;

@RestController
public class KafkaController {

   @Autowired
   private MessageProducer messageProducer;

   @GetMapping("/send")
   public String sendMessage(
      @RequestParam("message")
      String message
   ){
      messageProducer.sendMessage("my-topic", message);
      return "Message sent: " + message;
   }
}
