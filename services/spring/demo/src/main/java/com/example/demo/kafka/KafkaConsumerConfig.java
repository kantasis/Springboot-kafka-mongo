package com.example.demo.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConsumerConfig {

   @Value(value = "${spring.kafka.bootstrap-servers}")
   private String _kafkaBootstrap_addr;

   @Bean
   public ConsumerFactory<String,String> consumerFactory(){
      Map<String,Object> configProps = new HashMap<>();
      configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaBootstrap_addr);
      configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
      configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      System.out.println("GK> consumerFactory: "+_kafkaBootstrap_addr);
      return new DefaultKafkaConsumerFactory<>(configProps);
   }

   @Bean
   public ConcurrentKafkaListenerContainerFactory<String,String> kafkaListenerContainerFactory(){
      ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory());
      System.out.println("GK> kafkaListenerContainerFactory: "+_kafkaBootstrap_addr);
      return factory;
   }
}
