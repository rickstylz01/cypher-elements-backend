package com.example.cebackend.models.response;

import lombok.Data;

/**
 * A class representing a login response, containing a JWT token.
 */
@Data
public class LoginResponse {

  private String jwt;
}
