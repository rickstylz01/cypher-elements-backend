package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.User;
import com.example.cebackend.models.request.LoginRequest;
import com.example.cebackend.repository.UserRepository;
import com.example.cebackend.security.JWTUtils;
import com.example.cebackend.security.MyUserDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@MockitoSettings(strictness = Strictness.LENIENT) // keeps necessary stubbings for specific test cases
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JWTUtils jwtUtils;

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
  public void testEncodePassword() {
    // Arrange
    String rawPassword = "myPassword";
    String encodedPassword = "$2a$10$randomencodedpassword"; // mock encoded pw

    // Mock the behavior of the password encoder
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

    // Act
    String result = userService.encodePassword(rawPassword);

    // Assert
    assertEquals(encodedPassword, result);
  }

  @Test
  public void testCreatUserWhenEmailDoesNotExist() {
    // Arrange
    User user = new User();
    user.setEmailAddress("test@example.com");
    user.setPassword("password");

    when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(false);
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

    // Act
    User createdUser = userService.createUser(user);

    // Assert
    assertNotNull(createdUser);
    assertEquals("test@example.com", createdUser.getEmailAddress());
    assertEquals("encodedPassword", createdUser.getPassword());
  }

  @Test
  public void testLoginUserWithValidCredentials() {
    // Arrange
    String emailAddress = "test@example.com";
    String password = "password";

    User user = new User();
    user.setEmailAddress(emailAddress);
    user.setPassword(password);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmailAddress(user.getEmailAddress());
    loginRequest.setPassword(user.getPassword());

    // Mocking authentication manager behavior
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(emailAddress, password);
    Authentication authentication = new UsernamePasswordAuthenticationToken(new MyUserDetails(user), null);
    when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);

    // Mock JWT token generation
    when(jwtUtils.generateJwtToken(any(MyUserDetails.class))).thenReturn("validJwtToken");

    // Act
    Optional<String> jwtToken = userService.loginUser(loginRequest);

    // Assert
    assertTrue(jwtToken.isPresent());
    assertEquals("validJwtToken", jwtToken.get());
  }

  @Test
  public void testLoginUserWithInvalidCredentials() {
    // Arrange
    String emailAddress = "test@example.com";
    String password = "wrongPassword";

    User user = new User();
    user.setEmailAddress(emailAddress);
    user.setPassword("correctPassword"); // Set a different password in the user object

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmailAddress(emailAddress);
    loginRequest.setPassword(password);

    // Act
    Optional<String> jwtToken = userService.loginUser(loginRequest);

    // Assert
    assertFalse(jwtToken.isPresent());
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
