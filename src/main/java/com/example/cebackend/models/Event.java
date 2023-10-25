package com.example.cebackend.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDate eventDate;

    @Column
    private String venue;

    @Column
    private String description;

    @OneToMany(
      mappedBy = "event",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<Participant> participants;

    public Event() {
        this.participants = new ArrayList<>();
    }
}
