package com.example.cebackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTUtilsTest {
  @Mock
  private MyUserDetails myUserDetails;

  @InjectMocks
  private JWTUtils jwtUtils;

  // set the values of 'jwtExpMs' and 'jwtSecret' in 'JWTUtils to ensure the properties are properly injected for testing.
  @Before
  public void setUp() {
    ReflectionTestUtils.setField(jwtUtils, "jwtExpMs", 3600000);
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "C6UlILsE6GJwNqwCTkkvJj9O653yJUoteWMLfYyrc3vaGrrTOrJFAUD1wEBnnposzcQl");
  }

  @Test
  public void testGenerateJwtToken() {
    // Arrange
    String username = "testuser";
    when(myUserDetails.getUsername()).thenReturn(username);

    // Act
    String token = jwtUtils.generateJwtToken(myUserDetails);

    // Assert
    assertNotNull(token);

    // Verify the token is generated correctly
    String expectedToken = Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(new Date(new Date().getTime() + jwtUtils.getJwtExpMs()))
      .signWith(SignatureAlgorithm.HS256, jwtUtils.getJwtSecret())
      .compact();
    assertEquals(expectedToken, token);
  }
}
