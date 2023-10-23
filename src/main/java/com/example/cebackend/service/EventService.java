package com.example.cebackend.service;

import com.example.cebackend.models.Event;
import com.example.cebackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
  private final EventRepository eventRepository;

  @Autowired
  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public List<Event> getAllEvents() {
    return eventRepository.findAll();
  }

  public Optional<Event> getEventById(Long eventId) {
    return eventRepository.findById(eventId);
  }
}

