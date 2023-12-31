package com.example.cebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Participant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;
}