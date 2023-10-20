package com.example.cebackend.repository;

import com.example.cebackend.models.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryTest {

  @Autowired
  private EventRepository eventRepository;

  @Test
  public void testFindByName() {
    // Arrange
    Event event = new Event();
    event.setName("Test Event");
    event.setDescription("Sample event description");
    eventRepository.save(event);

    // Act
    Event foundEvent = eventRepository.findByName("Test Event");

    // Assert
    assertEquals("Sample event description", foundEvent.getDescription());
  }
}
