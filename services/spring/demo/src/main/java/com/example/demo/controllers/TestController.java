package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

   @GetMapping("/hello")
   public String jsonRetrieve() {
      System.out.println("=== GK > Called Hello");
      return "Hello world";
   }

}
