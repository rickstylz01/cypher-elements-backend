package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.User;
import com.example.cebackend.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {
  private final UserRepository userRepository;

  public User createUser(User userObject) {
    if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
      userRepository.save(userObject);

      return userObject;
    } else {
      throw new InformationExistException("user email address: " + userObject.getEmailAddress() + ", already exists.");
    }
  }

  public User findByUserEmailAddress(String emailAddress) {
    return userRepository.findUserByEmailAddress(emailAddress);
  }
}
