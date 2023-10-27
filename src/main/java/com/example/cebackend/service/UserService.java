package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.User;
import com.example.cebackend.models.request.LoginRequest;
import com.example.cebackend.repository.UserRepository;
import com.example.cebackend.security.JWTUtils;
import com.example.cebackend.security.MyUserDetails;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTUtils jwtUtils;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
    this.authenticationManager = authenticationManager;
  }

  public User createUser(User userObject) {
    if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
      String encodedPassword = encodePassword(userObject.getPassword());
      userObject.setPassword(encodedPassword);
      userRepository.save(userObject);
      return userObject;
    } else {
      throw new InformationExistException("user email address: " + userObject.getEmailAddress() + ", already exists.");
    }
  }

  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  public Optional<String> loginUser(LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
      return Optional.of(jwtUtils.generateJwtToken(myUserDetails));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Extracts user information from context holder
   * @return Current logged in User object
   */
  public User getCurrentLoggedInUser(){
    MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder //After jwt is generated, Security Context Holder is created to hold the user's state
      .getContext().getAuthentication().getPrincipal(); // the entire User object, with authentication details
    return userDetails.getUser();
  }

  public User findByUserEmailAddress(String emailAddress) {
    return userRepository.findUserByEmailAddress(emailAddress);
  }
}
