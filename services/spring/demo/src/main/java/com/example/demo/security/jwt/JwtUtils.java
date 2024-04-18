package com.example.demo.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import com.example.demo.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
   private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

   // Needs to be 64 characters long
   @Value("${custom.app.jwtSecret}")
   private String jwtSecret;
 
   @Value("${custom.app.jwtExpirationMs}")
   private int jwtExpirationMs;

   public String generateJwtToken(Authentication authentication){
      UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

      Date now_date = new Date();
      Date exp_date = new Date(now_date.getTime() + jwtExpirationMs);

      return Jwts.builder()
         .setSubject(userPrincipal.getUsername())
         .setIssuedAt(now_date)
         .setExpiration(exp_date)
         .signWith(key(), SignatureAlgorithm.HS256)
         .compact();
   }

   private Key key(){
      return Keys.hmacShaKeyFor(
         Decoders.BASE64.decode(jwtSecret)
      );
   }

   public String getUserNameFromJwtToken(String token){
      return Jwts.parserBuilder()
         .setSigningKey(key())
         .build()
         .parseClaimsJws(token)
         .getBody()
         .getSubject();
   }

   public boolean validateJwtToken(String token){
      try{
         Jwts.parserBuilder()
         .setSigningKey(key())
         .build()
         .parse(token);
         return true;
      }catch (MalformedJwtException e){
         logger.error("Invalid JWT token: {}",e.getMessage());
      }catch (ExpiredJwtException e){
         logger.error("Expired JWT token: {}",e.getMessage());
      }catch (UnsupportedJwtException e){
         logger.error("Unsupported JWT token: {}",e.getMessage());
      }catch (IllegalArgumentException e){
         logger.error("JWT claims string is empty: {}", e.getMessage());
      }
      return false;
   }

   

}
