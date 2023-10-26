package com.example.cebackend.models.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
  private Long eventId;
  private String emailAddress;
  private String eventName;
  private String venue;
  private String description;
  private List<EventParticipantDto> eventParticipantList;
}