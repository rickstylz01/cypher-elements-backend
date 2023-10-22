package com.example.cebackend.models.request;

import lombok.Getter;

/**
 * A class representing a login request, containing the user's email address and password.
 */
@Getter
public class LoginRequest {
  private String emailAddress;

  private String password;
}
