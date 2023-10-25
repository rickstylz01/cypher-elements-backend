package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.models.response.EventDTO;
import com.example.cebackend.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    List<EventDTO> returnedEvents = eventService.getAllEvents();

    // Assert
    assertEquals(2, returnedEvents.size());
    assertEquals(event1.getName(), returnedEvents.get(0).getName());
    assertEquals(event2.getName(), returnedEvents.get(1).getName());

  }

  @Test
  public void testGetEventById() {
    // Arrange
    Event event1 = new Event();
    event1.setId(1L);
    event1.setName("Event One");

    Event event2 = new Event();
    event2.setId(2L);
    event2.setName("Event Two");

    List<Event> events = List.of(event1, event2);

    // Mock the behavior of eventRepository.findAll()
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
    when(eventRepository.findById(2L)).thenReturn(Optional.of(event2));
    when(eventRepository.findById(3L)).thenReturn(Optional.empty());

    // Act
    Optional<Event> foundEvent1 = eventService.getEventById(1L);
    Optional<Event> foundEvent2 = eventService.getEventById(2L);
    Optional<Event> nonExistentEvent = eventService.getEventById(3L);

    // Assert
    assertTrue(foundEvent1.isPresent());
    assertEquals("Event One", foundEvent1.get().getName());

    assertTrue(foundEvent2.isPresent());
    assertEquals("Event Two", foundEvent2.get().getName());

    assertFalse(nonExistentEvent.isPresent());
  }

  @Test
  public void testCreateEvent() {
    // Arrange
    Event event1 = new Event();
    event1.setId(1L);
    event1.setName("Event One");
    event1.setEventDate(LocalDate.of(2023, 10, 23));
    event1.setVenue("Event Theater");
    event1.setDescription("Description for the Event Venue");

    // Mock eventRepository.save()
    when(eventRepository.save(event1)).thenReturn(event1);

    // Act
    Event createdEvent = eventService.createEvent(event1);

    // Assert
    assertNotNull(createdEvent);
    assertNotNull(createdEvent.getId());
    assertEquals(event1.getName(), createdEvent.getName());
    assertEquals(event1.getEventDate(), createdEvent.getEventDate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventWithNullEvent() {
    eventService.createEvent(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventWithNullName() {
    Event event = new Event();
    event.setName(null);
    eventService.createEvent(event);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventWithEmptyName() {
    Event event = new Event();
    event.setName("");
    eventService.createEvent(event);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventWithPastDate() {
    Event event = new Event();
    event.setEventDate(LocalDate.now().minusDays(1)); // Set a past date
    eventService.createEvent(event);
  }

  @Test
  public void testUpdateEvent() {
    // Arrange
    Long eventId = 1L;

    Event existingEvent = new Event();
    existingEvent.setId(eventId);
    existingEvent.setName("Old Event Name");

    Event updatedEvent = new Event();
    updatedEvent.setId(eventId);
    updatedEvent.setName("New Event Name");

    // Mock eventRepository.findById(id) and eventRepository.save(event)
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
    when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);

    // Act
    Event result = eventService.updateEvent(eventId, updatedEvent);

    // Assert
    assertNotNull(result);
    assertEquals("New Event Name", result.getName());
    verify(eventRepository).save(existingEvent);
  }

  @Test
  public void testDeleteEvent() {
    // Arrange
    Long eventId = 1L;

    // Act
    eventService.deleteEvent(eventId);

    // Assert
    verify(eventRepository, times(1)).deleteById(eventId);
  }
}
