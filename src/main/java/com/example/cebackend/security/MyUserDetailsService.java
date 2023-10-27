package com.example.cebackend.security;

import com.example.cebackend.models.User;
import com.example.cebackend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
    User user = userService.findByUserEmailAddress(emailAddress);
    return new MyUserDetails(user);
  }
}
