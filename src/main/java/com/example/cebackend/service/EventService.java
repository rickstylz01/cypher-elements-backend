package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public Event createEvent(Event event) {
    return eventRepository.save(event);
  }
}

