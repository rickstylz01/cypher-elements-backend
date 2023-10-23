package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventService eventService;

  @Test
  public void testGetAllEvents() {
    // Arrange
    Event event1 = new Event();
    event1.setId(1L);
    event1.setName("Event One");

    Event event2 = new Event();
    event2.setId(2L);
    event2.setName("Event Two");

    List<Event> events = List.of(event1, event2);

    // Mock the behavior of eventRepository.findAll()
    when(eventRepository.findAll()).thenReturn(events);

    // Act
    List<Event> returnedEvents = eventService.getAllEvents();

    // Assert
    assertEquals(2, returnedEvents.size());
    assertEquals(event1.getName(), returnedEvents.get(0).getName());
    assertEquals(event2.getName(), returnedEvents.get(1).getName());

  }
}
