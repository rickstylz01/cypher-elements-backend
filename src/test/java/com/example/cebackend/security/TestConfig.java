package com.example.cebackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test-application.properties")
public class TestConfig {
  @Value("${jwt-secret}")
  private String jwtSecret;

  @Value("${jwt-expiration-ms}")
  private int jwtExpMs;

  @Bean
  public String jwtSecret() {
    return jwtSecret;
  }

  @Bean
  public int jwtExpMs() {
    return jwtExpMs;
  }

}
