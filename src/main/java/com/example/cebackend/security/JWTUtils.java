package com.example.cebackend.security;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Data
public class JWTUtils {
  Logger logger = Logger.getLogger(JWTUtils.class.getName());

  @Value("${jwt-secret}")
  private String jwtSecret;

  @Value("${jwt-expiration-ms}")
  private int jwtExpMs;

  /**
   * Generate a JWT token based on user details.
   *
   * @param myUserDetails The user details for whom the token is generated.
   * @return A JWT token as a String.
   */
  public String generateJwtToken(MyUserDetails myUserDetails) {
    return Jwts.builder()
      .setSubject(myUserDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(new Date().getTime() + jwtExpMs))
      .signWith(SignatureAlgorithm.HS256, jwtSecret)
      .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(jwtSecret)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  /**
   * Validate the authenticity and expiration of a JWT token.
   *
   * @param token The JWT token to validate.
   * @return true if the token is valid, false otherwise.
   */
  public boolean validateJwtToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SecurityException e) {
      logger.log(Level.SEVERE, "Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.log(Level.SEVERE, "Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.log(Level.SEVERE, "JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.log(Level.SEVERE, "JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, "JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
