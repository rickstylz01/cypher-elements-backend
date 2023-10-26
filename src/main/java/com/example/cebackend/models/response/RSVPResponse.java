package com.example.cebackend.models.response;

import com.example.cebackend.models.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSVPResponse {
  private Long participantId;
  private String userName;
  private String emailAddress;
  private Long eventId;
  private String eventName;
  private String venue;
  private String description;
  private List<Participant> participantList;
}
