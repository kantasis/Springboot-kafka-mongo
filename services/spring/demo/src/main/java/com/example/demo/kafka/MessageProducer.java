package com.example.demo.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

   // @Autowired
   // private KafkaTemplate<String,String> kafkaTemplate;

   // public void sendMessage(String topic, String message){
   //    kafkaTemplate.send(topic,message);
   // }

   // // https://www.baeldung.com/spring-kafka
   // public void sendAsyncMessage(String topic, String message) {

   //    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

   //    future.whenComplete((result, ex) -> {
   //       if (ex == null) {
   //          System.out.println(
   //             "Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]"
   //          );
   //       } else {
   //          System.out.println(
   //             "Unable to send message=[" + message + "] due to : " + ex.getMessage()
   //          );
   //       }
   //    });
   
   // }
}
