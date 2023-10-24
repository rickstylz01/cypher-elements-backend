package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.ParticipantRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ParticipantService {

  private final ParticipantRepository participantRepository;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private static final Logger logger = Logger.getLogger(ParticipantService.class.getName());

  @Autowired
  public ParticipantService(ParticipantRepository participantRepository, UserRepository userRepository, EventRepository eventRepository) {
    this.participantRepository = participantRepository;
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
  }

  /**
   * Creates a new participant for a specific user and event.
   * @param userId The unique identifier of the user to be associated with the participant
   * @param eventId The unique identifier of the event to be associated with the participant.
   * @return The created Participant object after being saved in the participant repository.
   */
  public Participant createParticipant(Long userId, Long eventId) {
    logger.info("Creating participant for userId: " + userId + " and eventId: " + eventId);

    User user = userRepository.findById(userId)
      .orElseThrow(() -> {
        logger.severe("User not found with ID: " + userId);
        return new IllegalArgumentException("User not found with ID: " + userId);
      });

    Event event = eventRepository.findById(eventId)
      .orElseThrow(() -> {
        logger.severe("Event not found with ID: " + eventId);
        return new IllegalArgumentException("Event not found with ID: " + eventId);
      });

    Participant participant = new Participant();
    participant.setUser(user);
    participant.setEvent(event);
    Participant savedParticipant = participantRepository.save(participant);

    logger.info("Participant created with ID: " + savedParticipant.getId());

    return savedParticipant;
  }

  /**
   * Retrieves a list of participants for a specific event
   * @param eventId The unique identifier of the event for which participants are to be retrieved
   * @return A List of Participant objects associated with the specified event
   */
  public List<Participant> getParticipantsForEvents(Long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    return event.getParticipants();
  }

  /**
   * Retrieves a list of events in which a specific user is a participant.
   * @param userId The unique identifier of the user for whom events are to be retrieved
   * @return A List of Participant objects representing events in which the specified user is a participant
   */
  public List<Participant> getEventsForUser(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    return user.getParticipants();
  }

  /**
   * Deletes a participant from the system using its unique identifier
   * @param participantId The unique identifier of the participant to be deleted.
   */
  public void deleteParticipant(Long participantId) {
    Participant participant = participantRepository.findById(participantId)
      .orElseThrow(() -> new InformationNotFoundException("Participant with ID: " + participantId + ", not found."));

    participantRepository.delete(participant);
  }
}
