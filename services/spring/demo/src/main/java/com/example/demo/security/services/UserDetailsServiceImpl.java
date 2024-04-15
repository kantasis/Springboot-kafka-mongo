package com.example.demo.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   // TODO: Make this explicitly private
   UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      UserModel user = userRepository.findByUsername(username)
         .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username))
      ;
      
      return UserDetailsImpl.build(user);
   }


}