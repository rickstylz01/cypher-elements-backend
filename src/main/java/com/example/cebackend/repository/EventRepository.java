package com.example.cebackend.repository;

import com.example.cebackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  Event findByName(String name);
}
