package com.example.cebackend.repository;

import com.example.cebackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
  Event findByName(String name);

  @Query("SELECT e FROM Event e LEFT JOIN FETCH e.participants WHERE e.id = :eventId")
  Optional<Event> findEventWithParticipantsById(@Param("eventId") Long eventId);
}
