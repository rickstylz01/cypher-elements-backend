package com.example.cebackend.security;

import com.example.cebackend.models.User;
import com.example.cebackend.service.UserService;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Data
public class MyUserDetailsService implements UserDetailsService {
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
    User user = userService.findByUserEmailAddress(emailAddress);
    return new MyUserDetails(user);
  }
}
