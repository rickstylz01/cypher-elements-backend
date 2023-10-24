package com.example.cebackend.controllers;

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

  @PostMapping("/create/")
  public ResponseEntity<?> createEvent(@RequestBody Event event) {
    Event createdEvent = eventService.createEvent(event);
    message.put("message", "success");
    message.put("data", createdEvent);
    return new ResponseEntity<>(message, HttpStatus.CREATED);
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


}
