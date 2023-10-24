package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.ParticipantRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

  private final ParticipantRepository participantRepository;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  @Autowired
  public ParticipantService(ParticipantRepository participantRepository, UserRepository userRepository, EventRepository eventRepository) {
    this.participantRepository = participantRepository;
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
  }

  public Participant creatParticipant(Long userId, Long eventId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

    Participant participant = new Participant();
    participant.setUser(user);
    participant.setEvent(event);
    return participantRepository.save(participant);
  }

  public List<Participant> getParticipantsForEvents(Long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    return event.getParticipants();
  }

  public List<Participant> getEventsForUser(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    return user.getParticipants();
  }
}
