package com.example.cebackend.repository;

import com.example.cebackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
  Event findByName(String name);
}
