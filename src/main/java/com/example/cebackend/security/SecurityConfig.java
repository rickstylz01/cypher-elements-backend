package com.example.cebackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
      // user endpoints
      .antMatchers("/auth/users/login/", "/auth/users/register/").permitAll()
      // event endpoints
      .antMatchers(HttpMethod.GET,"/api/events/", "/api/events/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/events/").authenticated()
      .antMatchers(HttpMethod.PUT, "/api/events/**").authenticated()
      .antMatchers(HttpMethod.DELETE, "/api/events/**").authenticated()
      .antMatchers("/h2-console/**").permitAll()
      .anyRequest().authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session creation policy to STATELESS.
      .and()
      .csrf().disable()
      .headers().frameOptions().disable(); // Disable frame options for h2-console.
      http.cors(); // Enable CORS
    http.addFilterBefore(authJwtRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Add the JwtRequestFilter before the default UsernamePasswordAuthenticationFilter.
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
