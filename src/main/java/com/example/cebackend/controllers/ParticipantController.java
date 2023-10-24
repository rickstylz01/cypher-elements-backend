package com.example.cebackend.controllers;

import com.example.cebackend.exceptions.InformationExistException;
import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.response.RSVPResponse;
import com.example.cebackend.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
  private final ParticipantService participantService;
  static HashMap<String, Object> message = new HashMap<>();

  @Autowired
  public ParticipantController(ParticipantService participantService) {
    this.participantService = participantService;
  }

  @PostMapping("/")
  public ResponseEntity<?> createParticipant(@RequestParam Long userId, @RequestParam Long eventId) {
    try {
      RSVPResponse participant = participantService.createParticipant(userId, eventId);
      message.put("message", "success, participant created");
      message.put("data", participant);
      return new ResponseEntity<>(message, HttpStatus.CREATED);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    } catch (InformationExistException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/{userId}/")
  public ResponseEntity<?> getEventsForUser(@PathVariable Long userId) {
    try {
      List<RSVPResponse> events = participantService.getEventsForUser(userId);
      message.put("message", "success");
      message.put("data", events);
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/event/{eventId}/")
  public ResponseEntity<?> getParticipantsForEvent(@PathVariable Long eventId) {
    try {
      List<RSVPResponse> participants = participantService.getParticipantsForEvents(eventId);
      message.put("message", "success");
      message.put("data", participants);
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{participantId}/event/{eventId}/")
  public ResponseEntity<?> deleteParticipant(@PathVariable Long participantId, @PathVariable Long eventId) {
    try {
      participantService.deleteParticipant(participantId, eventId);
      message.put("message", "participant with ID: " + participantId + ", successfully deleted");
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (InformationNotFoundException e) {
      message.put("message", e.getMessage());
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
  }
}
