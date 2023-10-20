package com.example.cebackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
@Data
public class JWTUtils {
  Logger logger = Logger.getLogger(JWTUtils.class.getName());

  @Value("${jwt-secret}")
  private String jwtSecret;

  @Value("${jwt-expiration-ms}")
  private int jwtExpMs;

  public String generateJwtToken(MyUserDetails myUserDetails) {
    return Jwts.builder()
      .setSubject(myUserDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(new Date().getTime() + jwtExpMs))
      .signWith(SignatureAlgorithm.HS256, jwtSecret)
      .compact();
  }
}
