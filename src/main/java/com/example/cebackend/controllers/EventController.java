package com.example.cebackend.controllers;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.Event;
import com.example.cebackend.models.response.EventDTO;
import com.example.cebackend.models.response.EventResponseDto;
import com.example.cebackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {
  private final EventService eventService;

  private static final Logger logger = Logger.getLogger(EventController.class.getName());

  static HashMap<String, Object> message = new HashMap<>();

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/")
  public ResponseEntity<?> createEvent(@RequestBody Event event) {
    try {
      Event createdEvent = eventService.createEvent(event);
      message.put("message", "success, event created");
      message.put("data", createdEvent);
      return new ResponseEntity<>(message, HttpStatus.CREATED);
    } catch (InformationExistException e) {
      message.put("message", "unable to create event");
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

  }

  @PostMapping("/{eventId}/rsvp/{userId}/")
  public ResponseEntity<?> rsvpToEvent(@PathVariable Long eventId, @PathVariable Long userId) {
    try {
      EventResponseDto participant = eventService.rsvpToEvent(eventId, userId);
      message.put("message", "success, RSVP confirmed");
      message.put("data", participant);
      return new ResponseEntity<>(message, HttpStatus.CREATED);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error processing RSVP", e);
      message.put("message", "Error processing RSVP");
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/")
  public ResponseEntity<?> getAllEvents() {
    List<EventDTO> events = eventService.getAllEvents();
    message.put("message", "success");
    message.put("data", events);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  // Get an event by its ID
  @GetMapping("/{eventId}/")
  public ResponseEntity<HashMap<String, Object>> getEventById(@PathVariable Long eventId) {
    HashMap<String, Object> response = new HashMap<>();
    try {
      Optional<EventDTO> eventDTO = eventService.getEventById(eventId);
      response.put("message", "success");
      response.put("data", eventDTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InformationNotFoundException e) {
      response.put("message", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{eventId}/")
  public ResponseEntity<?> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO updatedEventDTO) {
    try {
      EventDTO eventDTO = eventService.updateEvent(eventId, updatedEventDTO);
      message.put("message", "success");
      message.put("data", eventDTO);
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>("Error updating event", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{eventId}/")
  public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
    try {
      eventService.deleteEvent(eventId);
      message.put("message", "Event with id: " + eventId  + ", successfully deleted");
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>("Error deleting event", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{eventId}/participants/{participantId}/")
  public ResponseEntity<?> removeParticipantFromEvent(@PathVariable Long eventId, @PathVariable Long participantId) {
    try {
      eventService.removeParticipantFromEvent(eventId, participantId);
      message.put("message", "Participant with id: " + participantId + " removed from event with id: " + eventId);
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      message.put("message", "Error removing participant from event");
      return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
