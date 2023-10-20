package com.example.cebackend.service;

import com.example.cebackend.models.User;
import com.example.cebackend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void testCreatUser() {
    // Arrange
    User user = new User();
    user.setEmailAddress("test@example.com");
    user.setPassword("password");
    user.setUserName("testUser");

    when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    // Act
    User createdUser = userService.createUser(user);

    // Assert
    assertNotNull(createdUser);
    assertEquals("test@example.com", createdUser.getEmailAddress());
    assertEquals("password", createdUser.getPassword());
    assertEquals("testUser", createdUser.getUserName());
  }

  @Test
  public void testFindByUserEmailAddressWithValidEmail() {
    // Arrange
    String emailAddress = "test@example.com";
    User mockUser = new User();
    when(userRepository.findUserByEmailAddress(emailAddress)).thenReturn(mockUser);

    // Act
    User foundUser = userService.findByUserEmailAddress(emailAddress);

    // Assert
    assertNotNull(foundUser);
    assertEquals(mockUser, foundUser);
  }
}
