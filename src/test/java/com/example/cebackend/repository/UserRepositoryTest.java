package com.example.cebackend.repository;

import com.example.cebackend.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindUserByEmailAddress() {
    // Arrange
    User user = new User();
    user.setUserName("testuser");
    user.setEmailAddress("test@example.com");
    user.setPassword("testpassword");
    userRepository.save(user);

    // Act
    Optional<User> foundUserOptional = Optional.ofNullable(userRepository.findUserByEmailAddress("test@example.com"));

    // Assert
    assertTrue(foundUserOptional.isPresent()); // Check if the optional contains a value

    User foundUser = foundUserOptional.orElseThrow(); // Get the user from Optional

    assertEquals("testuser", foundUser.getUserName());
    assertEquals("testpassword", foundUser.getPassword());
  }
}
