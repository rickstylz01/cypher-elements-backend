package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
  private final EventRepository eventRepository;

  @Autowired
  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  /**
   * Retrieves a list of all events from the event repository
   * @return A List of Event objects representing all events in the repository
   */
  public List<Event> getAllEvents() {
    return eventRepository.findAll();
  }

  /**
   * Retrieves an event by its unique identifier
   * @param eventId The unique identifier of the even to be retrieved
   * @return An Optional containing the Event object if found, or an empty Optional if not found
   */
  public Optional<Event> getEventById(Long eventId) {
    return eventRepository.findById(eventId);
  }

  /**
   * Creates a new event and saves it to the event repository
   * @param event The Event object containing information about the event
   * @return The Event object after being saved in the event repository
   */
  public Event createEvent(Event event) {
    // Check for null event
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    // Check for empty or null event name
    if (event.getName() == null || event.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }

    // Check for invalid event date (past date)
    if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Event date cannot be in the past");
    }

    return eventRepository.save(event);
  }


}

