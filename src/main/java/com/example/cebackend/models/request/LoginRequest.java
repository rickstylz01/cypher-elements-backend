package com.example.cebackend.models.request;

import lombok.Data;

/**
 * A class representing a login request, containing the user's email address and password.
 */
@Data
public class LoginRequest {
  private String emailAddress;

  private String password;
}
