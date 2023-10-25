package com.example.cebackend.models.response;

import com.example.cebackend.models.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
  private Long id;
  private String name;
  private LocalDate eventDate;
  private String venue;
  private String description;
  private List<RSVPResponse> participants;

  public EventDTO(Event event) {
    this.id = event.getId();
    this.name = event.getName();
    this.eventDate = event.getEventDate();
    this.venue = event.getVenue();
    this.description = event.getDescription();
    this.participants = event.getParticipants().stream()
      .map(participant -> new RSVPResponse(
        participant.getId(),
        participant.getUser().getUserName(),
        participant.getUser().getEmailAddress(),
        participant.getEvent().getId(),
        participant.getEvent().getName(),
        participant.getEvent().getVenue(),
        participant.getEvent().getDescription()))
      .collect(Collectors.toList());
  }
}
