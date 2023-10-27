package com.example.cebackend.models.response;

import com.example.cebackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A class representing a login response, containing a JWT token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponse {

  private String jwt;
  public User user;

  public LoginResponse(String jwt) {
    this.jwt = jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }
}
