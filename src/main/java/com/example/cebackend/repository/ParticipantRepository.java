package com.example.cebackend.repository;

import com.example.cebackend.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
  List<Participant> findByUserId(Long userId);
  Optional<Participant> findById(Long participantId);
}
