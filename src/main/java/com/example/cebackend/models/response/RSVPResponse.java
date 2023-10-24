package com.example.cebackend.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSVPResponse {
  private String userName;
  private String emailAddress;
  private Long eventId;
  private String eventName;
  private String venue;
  private String description;
}
