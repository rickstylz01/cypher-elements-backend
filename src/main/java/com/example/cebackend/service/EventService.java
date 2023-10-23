package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationNotFoundException;
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

  /**
   * Update an existing event with the provided event ID using the details from the updated event.
   * @param eventId The unique identifier of the event to be updated
   * @param updatedEvent The Event object containing updated information to be applied to the existing event
   * @return The updated Event object after modifications, saved back in the event repository
   */
  public Event updateEvent(Long eventId, Event updatedEvent) {
    // Check for valid eventId
    if (eventId == null || eventId <= 0) {
      throw new IllegalArgumentException("Invalid eventId");
    }

    Optional<Event> existingEventOptional = eventRepository.findById(eventId);
    if (existingEventOptional.isPresent()) {
      Event existingEvent = existingEventOptional.get();

      // Checking for null or empty name in the updatedEvent
      if (updatedEvent.getName() != null && !updatedEvent.getName().trim().isEmpty()) {
        existingEvent.setName(updatedEvent.getName());
      }

      // Checking for null or empty eventDate
      if (updatedEvent.getEventDate() != null && updatedEvent.getEventDate().isAfter(LocalDate.now())) {
        existingEvent.setEventDate(updatedEvent.getEventDate());
      }

      // Checking for null or empty venue
      if (updatedEvent.getVenue() != null && !updatedEvent.getVenue().trim().isEmpty()) {
        existingEvent.setVenue(updatedEvent.getVenue());
      }

      if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().trim().isEmpty()) {
        existingEvent.setDescription(updatedEvent.getDescription());
      }

      return eventRepository.save(existingEvent);
    } else {
      throw new InformationNotFoundException("Event with id: " + eventId + ", not found.");
    }
  }

  /**
   * Deletes an event from the system using its unique identifier
   * @param eventId The unique identifier of the event to be deleted.
   */
  public void deleteEvent(Long eventId) {
    // Check for valid eventId
    if (eventId == null || eventId <= 0) {
      throw new IllegalArgumentException("Invalid eventId");
    }

    eventRepository.deleteById(eventId);
  }

}

