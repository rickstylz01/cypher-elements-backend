package com.example.cebackend.controllers;

import com.example.cebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/auth/users")
public class UserController {
  Logger logger = Logger.getLogger(UserController.class.getName());
  private UserService userService;
  static HashMap<String, Object> message = new HashMap<>();

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/hello/")
  public ResponseEntity<?> getHello() {
    message.put("message", "Hello");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
