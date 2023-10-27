package com.example.cebackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;

    @Column
    private String name;

    @Column
    private LocalDate eventDate;

    @Column
    private String venue;

    @Column
    private BigDecimal price;

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