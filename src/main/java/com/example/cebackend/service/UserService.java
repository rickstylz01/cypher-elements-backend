package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.User;
import com.example.cebackend.models.request.LoginRequest;
import com.example.cebackend.repository.UserRepository;
import com.example.cebackend.security.JWTUtils;
import com.example.cebackend.security.MyUserDetails;
import lombok.Data;
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

  public User findByUserEmailAddress(String emailAddress) {
    return userRepository.findUserByEmailAddress(emailAddress);
  }
}
