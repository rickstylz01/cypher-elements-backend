package com.example.cebackend.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RSVPResponse {
  private Long userId;
  private String userName;
  private String emailAddress;
}
