package com.example.cebackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtRequestFilter authJwtRequestFilter() {
    return new JwtRequestFilter();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/auth/users/login/", "/auth/users/register/").permitAll()
      .antMatchers("/api/events/", "/api/events/**").permitAll()
//      .antMatchers("/api/events/create/").authenticated()
      .antMatchers("/h2-console/**").permitAll()
      .antMatchers("/auth/users/hello/").permitAll()
      .anyRequest().authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session creation policy to STATELESS.
      .and()
      .csrf().disable()
      .headers().frameOptions().disable(); // Disable frame options for h2-console.
      http.cors(); // Enable CORS
    // Add the JwtRequestFilter before the default UsernamePasswordAuthenticationFilter.
    http.addFilterBefore(authJwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
