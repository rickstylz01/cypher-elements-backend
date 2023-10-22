package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.User;
import com.example.cebackend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void testCreatUserWhenEmailDoesNotExist() {
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

  @Test(expected = InformationExistException.class)
  public void testCreateUserWhenEmailExists() {
    // Arrange
    User existingUser = new User();
    existingUser.setEmailAddress("test@example.com");
    existingUser.setPassword("existingPassword");
    existingUser.setUserName("existingUser");

    when(userRepository.existsByEmailAddress(existingUser.getEmailAddress())).thenReturn(true);

    // Act
    userService.createUser(existingUser);

    // Assert
    // Expect Exception
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

  @Test
  public void testFindByUserEmailAddressWithNonExistentEmail() {
    // Arrange
    String nonExistentEmail = "nonexistentemail@example.com";
    when(userRepository.findUserByEmailAddress(nonExistentEmail)).thenReturn(null);

    // Act
    User foundUser = userService.findByUserEmailAddress(nonExistentEmail);

    // Assert
    assertNull(foundUser);
  }
}
