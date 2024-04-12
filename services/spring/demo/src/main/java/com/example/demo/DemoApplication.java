package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mongo.DataModel;
import com.example.demo.mongo.DataRepository;

@SpringBootApplication
public class DemoApplication {

   public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
   }

   @RestController
   class HelloController {

      @GetMapping("/hello")
      public String hello() {
         return "Hello, Spring Boot!";
      }
   }

   // @Bean
   // CommandLineRunner runner(DataRepository dataRepository){
   //    return args ->{
   //       DataModel dataModel = new DataModel("NEW DATA");
   //       dataRepository.insert(dataModel);
   //    };
   // }

}
