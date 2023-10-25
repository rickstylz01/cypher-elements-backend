package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.models.response.EventDTO;
import com.example.cebackend.models.response.RSVPResponse;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.ParticipantRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EventService {
  private final EventRepository eventRepository;
  private final ParticipantRepository participantRepository;
  private final UserRepository userRepository;
  private static final Logger logger = Logger.getLogger(EventService.class.getName());


  @Autowired
  public EventService(EventRepository eventRepository, ParticipantRepository participantRepository, UserRepository userRepository) {
    this.eventRepository = eventRepository;
    this.participantRepository = participantRepository;
    this.userRepository = userRepository;
  }

  /**
   * Retrieves a list of all events from the event repository
   * @return A List of Event objects representing all events in the repository
   */
  public List<EventDTO> getAllEvents() {
    List<Event> events = eventRepository.findAll();
    return events.stream()
      .map(event -> convertEventToEventDTO(event))
      .collect(Collectors.toList());

  }

  private EventDTO convertEventToEventDTO(Event event) {
    EventDTO eventDTO = new EventDTO();
    eventDTO.setId(event.getId());
    eventDTO.setName(event.getName());
    eventDTO.setEventDate(event.getEventDate());
    eventDTO.setVenue(event.getVenue());
    eventDTO.setDescription(event.getDescription());

    // Convert Participants to RSVPResponse objects
    List<RSVPResponse> participants = event.getParticipants().stream()
        .map(participant -> createRSVPResponse(participant))
          .collect(Collectors.toList());

    eventDTO.setParticipants(participants);
    return eventDTO;
  }

  //  /**
//   * Creates an RSVPResponse object based on participant details.
//   * @param participant The Participant object from which to create the RSVPResponse
//   * @return The created RSVPResponse object with user and event details
//   */
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
   * Retrieves an event by its unique identifier
   * @param eventId The unique identifier of the even to be retrieved
   * @return An Optional containing the Event object if found, or an empty Optional if not found
   */
  public Optional<EventDTO> getEventById(Long eventId) {
    Optional<Event> eventOptional = eventRepository.findById(eventId);
    if (eventOptional.isPresent()) {
      Event event = eventOptional.get();
      return Optional.of(new EventDTO(event));
    } else {
      throw new InformationNotFoundException("Event with ID: " + eventId + ", not found");
    }
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

  /**
   * Retrieves a user by their unique identifier from the user repository
   * @param userId The unique identifier of the user to be retrieved
   * @return The User object associated with the provided ID
   */
  private User getUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new InformationNotFoundException("User with ID: " + userId + ", not found"));
  }

  private Event convertEventDTOToEvent(EventDTO eventDTO) {
    Event event = new Event();
    event.setId(eventDTO.getId());
    event.setName(eventDTO.getName());
    event.setEventDate(eventDTO.getEventDate());
    event.setVenue(eventDTO.getVenue());
    event.setDescription(eventDTO.getDescription());
    // You may need to convert other fields as well
    return event;
  }

  /**
   * Handles RSVP for an event, creating a participant for the specified user and event.
   *
   * @param eventId The unique identifier of the event.
   * @param userId  The unique identifier of the user who is RSVPing.
   * @return The created Participant object after being saved in the participant repository.
   */
  public RSVPResponse rsvpToEvent(Long eventId, Long userId) {
    if (eventId == null || eventId <= 0 || userId == null || userId <= 0) {
      throw new IllegalArgumentException("Invalid evenId or userId");
    }

    Optional<EventDTO> eventOptional = getEventById(eventId);

    if (eventOptional.isPresent()) {
      EventDTO eventDTO = eventOptional.get();

      // Convert EventDTO to Event entity
      Event event = convertEventDTOToEvent(eventDTO);

      Optional<User> userOptional = userRepository.findById(userId);

      if (userOptional.isPresent()) {
        User user = userOptional.get();

        // Create a participant
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setEvent(event);

        // Save participant to the repository
        Participant savedParticipant = participantRepository.save(participant);

        return createRSVPResponse(savedParticipant);
      } else {
        throw new InformationNotFoundException("User not found with ID: " + userId);
      }
    } else {
      throw new InformationNotFoundException("Event with ID: " + eventId + ", not found");
    }
  }

}

