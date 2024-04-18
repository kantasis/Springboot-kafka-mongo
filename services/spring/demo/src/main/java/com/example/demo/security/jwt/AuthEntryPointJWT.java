package com.example.demo.security.jwt;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthEntryPointJWT implements AuthenticationEntryPoint{
   private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJWT.class);

   @Override
   // This method will be triggerd anytime unauthenticated User requests a secured HTTP resource and an AuthenticationException is thrown.
   // Handle Authentication Exception
   public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authenticationException
   ) throws IOException, ServletException {
      logger.error("Unauthorized error: {}", authenticationException.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED,  "Error: Unauthorized");
      // HttpServletResponse.SC_UNAUTHORIZED is the 401 Status code. It indicates that the request requires HTTP authentication
   }
}
