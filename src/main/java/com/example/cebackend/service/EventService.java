package com.example.cebackend.service;

import com.example.cebackend.exceptions.InformationNotFoundException;
import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.models.response.EventDTO;
import com.example.cebackend.models.response.EventParticipantDto;
import com.example.cebackend.models.response.EventResponseDto;
import com.example.cebackend.models.response.RSVPResponse;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.ParticipantRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

  private void validateEvent(Event event) {
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
  }

  private void validateEventId(Long eventId) {
    if (eventId == null || eventId <= 0) {
      throw new IllegalArgumentException("Invalid eventId");
    }
  }

  /**
   * Retrieves a user by their unique identifier from the user repository
   *
   * @param userId The unique identifier of the user to be retrieved
   * @return The User object associated with the provided ID
   */
  private User getUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new InformationNotFoundException("User with ID: " + userId + ", not found"));
  }

  /**
   * Creates an RSVPResponse object based on participant details.
   *
   * @param participant The Participant object from which to create the RSVPResponse
   * @return The created RSVPResponse object with user and event details
   */
  private RSVPResponse createRSVPResponse(Participant participant) {
    RSVPResponse rsvpResponse = new RSVPResponse();
    User user = participant.getUser();

    rsvpResponse.setUserName(user.getUserName());
    rsvpResponse.setEmailAddress(user.getEmailAddress());

    logger.info("Participant created with ID: " + participant.getId());
    return rsvpResponse;
  }

  /**
   * Converts an EventDTO object to its corresponding Event representation
   *
   * @param eventDTO The EventDTO object to be converted to Event
   * @return An Event object representing the provided EventDTO
   */
  Event convertEventDTOToEvent(EventDTO eventDTO) {
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
   * Converts an Event object to its corresponding EventDTO representation
   *
   * @param event The Event object to be converted to EventDTO
   * @return An EventDTO object representing the provided Event with participants converted to RSVPResponse objects.
   */
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


  private EventResponseDto createEventResponseDto(Event event, User user) {
    EventResponseDto responseDto = new EventResponseDto();
    responseDto.setEventId(event.getId());
    responseDto.setEmailAddress(user.getEmailAddress());
    responseDto.setEventName(event.getName());
    responseDto.setVenue(event.getVenue());
    responseDto.setDescription(event.getDescription());

    List<EventParticipantDto> eventParticipantList = event.getParticipants().stream()
      .map(participant -> createEventParticipantDto(participant))
      .collect(Collectors.toList());

    responseDto.setEventParticipantList(eventParticipantList);
    return responseDto;
  }

  private EventParticipantDto createEventParticipantDto(Participant participant) {
    return new EventParticipantDto(
      participant.getUser().getId(),
      participant.getUser().getUserName(),
      participant.getUser().getEmailAddress()
    );
  }





  /**
   * Retrieves a list of all events from the event repository
   *
   * @return A List of Event objects representing all events in the repository
   */
  public List<EventDTO> getAllEvents() {
    List<Event> events = eventRepository.findAll();
    return events.stream()
      .map(this::convertEventToEventDTO)
      .collect(Collectors.toList());
  }

  /**
   * Creates a new event and saves it to the event repository
   *
   * @param event The Event object containing information about the event
   * @return The Event object after being saved in the event repository
   */
  public Event createEvent(Event event) {
    validateEvent(event);
    return eventRepository.save(event);
  }

  /**
   * Updates an existing event with the provided event ID using details from the updated EventDTO
   *
   * @param eventId         The unique identifier of the event to be updated
   * @param updatedEventDTO The EventDTO containing updated information to be applied to the existing event.
   * @return The updated EventDTO object representing the modified event after being saved back in the event repository.
   */
  public EventDTO updateEvent(Long eventId, EventDTO updatedEventDTO) {
    validateEventId(eventId);

    Optional<Event> existingEventOptional = eventRepository.findById(eventId);
    if (existingEventOptional.isPresent()) {
      Event existingEvent = existingEventOptional.get();
      Event updatedEvent = convertEventDTOToEvent(updatedEventDTO);

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

      Event savedEvent = eventRepository.save(existingEvent);

      //Convert and return updated event
      return new EventDTO(savedEvent);
    } else {
      throw new InformationNotFoundException("Event with id: " + eventId + ", not found.");
    }
  }

  /**
   * Handles RSVP for an event, creating a participant for the specified user and event.
   *
   * @param eventId The unique identifier of the event.
   * @param userId  The unique identifier of the user who is RSVPing.
   * @return The created Participant object after being saved in the participant repository.
   */
  public EventResponseDto rsvpToEvent(Long eventId, Long userId) {
    validateEventId(eventId);

    Event event = getEventById(eventId)
      .orElseThrow(() -> new InformationNotFoundException("Event with ID: " + eventId + ", not found"));

    User user = getUserById(userId);

    boolean isAlreadyParticipant = event.getParticipants().stream()
      .anyMatch(participant -> participant.getUser().getId().equals(userId));

    if (isAlreadyParticipant) {
      throw new IllegalArgumentException("User with ID: " + userId + " is already a participant in this event");
    }

    Participant participant = new Participant();
    participant.setUser(user);
    participant.setEvent(event);
    participantRepository.save(participant);

    return createEventResponseDto(event, user);
  }

  /**
   * Removes a participant from an event based on their unique participant ID
   *
   * @param eventId       The unique identifier of the event from which the participant should be removed.
   * @param participantId The unique identifier of the participant to be removed from the event.
   */
  public void removeParticipantFromEvent(Long eventId, Long participantId) {
    validateEventId(eventId);

    Event event = eventRepository.findById(eventId)
      .orElseThrow(() -> new InformationNotFoundException("Event with ID: " + eventId + ", not found"));

    // remove participant from the event
    event.getParticipants().removeIf(participant -> participant.getId().equals(participantId));

    // Save the updated event back to the repository
    eventRepository.save(event);
  }

  /**
   * Retrieves an event by its unique identifier
   *
   * @param eventId The unique identifier of the even to be retrieved
   * @return An Optional containing the Event object if found, or an empty Optional if not found
   */
  public Optional<Event> getEventById(Long eventId) {
    Optional<Event> eventOptional = eventRepository.findById(eventId);
    if (eventOptional.isPresent()) {
      Event event = eventOptional.get();
      return Optional.of(event);
    } else {
      throw new InformationNotFoundException("Event with ID: " + eventId + ", not found");
    }
  }

  /**
   * Deletes an event from the system using its unique identifier
   *
   * @param eventId The unique identifier of the event to be deleted.
   */
  public void deleteEvent(Long eventId) {
    // Check for valid eventId
    validateEventId(eventId);
    eventRepository.deleteById(eventId);
  }
}