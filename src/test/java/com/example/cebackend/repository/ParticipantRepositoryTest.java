package com.example.cebackend.repository;

import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParticipantRepositoryTest {

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindByUserId() {
    // Arrange
    User user = new User();
    user.setUserName("newtestuser");
    user.setEmailAddress("test2@example.com");
    user.setPassword("testpassword");
    userRepository.save(user);

    Participant participant = new Participant();
    participant.setUser(user);
    participantRepository.save(participant);

    // Act
    List<Participant> participants = participantRepository.findByUserId(user.getId());

    // Assert
    assertEquals(1, participants.size());
    assertEquals("newtestuser", participants.get(0).getUser().getUserName());
  }

  @Test
  public void testFindById() {
    // Arrange
    User user = new User();
    user.setUserName("anothertestuser");
    user.setEmailAddress("test3@example.com");
    user.setPassword("testpassword");
    userRepository.save(user);

    Participant participant = new Participant();
    participant.setUser(user);
    participantRepository.save(participant);

    // Act: Retrieve the participent from the database by its ID
    Optional<Participant> foundParticipantOptional = participantRepository.findById(participant.getId());

    // Assert: Check if the participant is present and has correct properties
    assertTrue(foundParticipantOptional.isPresent());
    Participant foundParticipant = foundParticipantOptional.get();

    assertEquals(user.getUserName(), foundParticipant.getUser().getUserName());
    assertEquals(user.getEmailAddress(), foundParticipant.getUser().getEmailAddress());
    assertEquals(participant.getId(), foundParticipant.getId());
  }
}

