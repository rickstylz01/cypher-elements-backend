package com.example.cebackend.seeder;

import com.example.cebackend.models.User;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserDataLoader(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    loadUserData();
  }

  private void loadUserData() {
    if (userRepository.count() == 0) {
      User user1 = new User();
      user1.setUserName("newuser1");
      user1.setEmailAddress("test@example.com");
      user1.setPassword(passwordEncoder.encode("password123"));
      userRepository.save(user1);
    }
  }
}