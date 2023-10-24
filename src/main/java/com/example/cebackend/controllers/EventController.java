package com.example.cebackend.controllers;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.models.Event;
import com.example.cebackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {
  private final EventService eventService;
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

  @GetMapping("/")
  public ResponseEntity<?> getAllEvents() {
    List<Event> events = eventService.getAllEvents();
    message.put("message", "success");
    message.put("data", events);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @GetMapping("/{eventId}/")
  public ResponseEntity<HashMap<String, Object>> getEventById(@PathVariable Long eventId) {
    HashMap<String, Object> response = new HashMap<>();
    Optional<Event> eventOptional = eventService.getEventById(eventId);
    if (eventOptional.isPresent()) {
      Event event = eventOptional.get();
      response.put("message", "success");
      response.put("data", event);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      response.put("message", "Event not found");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{eventId}/")
  public ResponseEntity<?> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
    try {
      Event event = eventService.updateEvent(eventId, updatedEvent);
      message.put("message", "success");
      message.put("data", event);
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
      return new ResponseEntity<>("Error deleteing event", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
