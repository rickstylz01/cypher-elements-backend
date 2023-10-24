package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.models.response.RSVPResponse;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.ParticipantRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
  public RSVPResponse createParticipant(Long userId, Long eventId) {
    logger.info("Creating participant for userId: " + userId + " and eventId: " + eventId);

    User user = getUserById(userId);
    Event event = getEventById(eventId);

    Participant participant = new Participant();
    participant.setUser(user);
    participant.setEvent(event);
    Participant savedParticipant = participantRepository.save(participant);

    return createRSVPResponse(savedParticipant);
  }

  /**
   * Retrieves a user by their unique identifier from the user repository
   * @param userId The unique identifier of the user to be retrieved
   * @return The User object associated with the provided ID
   */
  private User getUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new InformationNotFoundException("User with ID: " + userId + ", not found"));
  }

  /**
   * Retrieves an event by its unique identifier from the event repository
   * @param eventId The unique identifier of the event to be retrieved
   * @return The Event object associated with the provided ID
   */
  private Event getEventById(Long eventId) {
    return eventRepository.findById(eventId)
      .orElseThrow(() -> new InformationNotFoundException("User with ID: " + eventId + ", not found"));
  }

  /**
   * Creates an RSVPResponse object based on participant details.
   * @param participant The Participant object from which to create the RSVPResponse
   * @return The created RSVPResponse object with user and event details
   */
  private RSVPResponse createRSVPResponse(Participant participant) {
    RSVPResponse rsvpResponse = new RSVPResponse();
    User user = participant.getUser();
    Event event = participant.getEvent();

    rsvpResponse.setUserName(user.getUserName());
    rsvpResponse.setEmailAddress(user.getEmailAddress());
    rsvpResponse.setEventId(event.getId());
    rsvpResponse.setEventName(event.getName());
    rsvpResponse.setVenue(event.getVenue());
    rsvpResponse.setDescription(event.getDescription());

    logger.info("Participant created with ID: " + participant.getId());
    return rsvpResponse;
  }

  /**
   * Retrieves a list of RSVPResponse objects representing participants for a specific event
   * @param eventId The unique identifier of the event for which participants are to be retrieved
   * @return A List of Participant objects associated with the specified event
   */
  public List<RSVPResponse> getParticipantsForEvents(Long eventId) {
    Event event = eventRepository.findById(eventId)
      .orElseThrow(() -> new InformationNotFoundException("Event not found with ID: " + eventId));

    List<RSVPResponse> responseList = new ArrayList<>();
    for (Participant participant : event.getParticipants()) {
      RSVPResponse rsvpResponse = createRSVPResponse(participant);
      responseList.add(rsvpResponse);
    }

    return responseList;
  }

  /**
   * Retrieves a list of RSVPResponse objects representing events in which a user is a participant
   * @param userId The unique identifier of the user for whom events are to be retrieved
   * @return A List of RSVPResponse objects representing events in which the specified user is a participant
   */
  public List<RSVPResponse> getEventsForUser(Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new InformationNotFoundException("User not found with ID: " + userId));

    List<RSVPResponse> responseList = new ArrayList<>();
    for (Participant participant : user.getParticipants()) {
      RSVPResponse rsvpResponse = createRSVPResponse(participant);
      responseList.add(rsvpResponse);
    }

    return responseList;
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
