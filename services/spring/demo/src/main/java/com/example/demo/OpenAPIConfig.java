package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

   // TODO: These two should be static and final
   @Value("${custom.openapi.dev-url}")
   private String devUrl;

   @Value("${custom.openapi.prod-url}")
   private String prodUrl;

   @Bean
   public OpenAPI myOpenAPI() {
      Server devServer = new Server();
      devServer.setUrl(devUrl);
      devServer.setDescription("Server URL in Dev environment");

      Server prodServer = new Server();
      prodServer.setUrl(prodUrl);
      prodServer.setDescription("Server URL in Prod environment");

      Contact contact = new Contact();
      contact.setEmail("george.kantasis@iti.gr");
      contact.setName("George kantasis");

      // License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

      Info info = new Info()
         .title("ONCOSCREEN Datalake API")
         .version("1.0")
         .contact(contact)
         .description("This API exposes endpoints to manage the datalake for the ONCOSCREEN project")
         // .license(mitLicense)
      ;

      return new OpenAPI()
         .info(info)
         .servers( List.of(devServer, prodServer) );
   }

}



