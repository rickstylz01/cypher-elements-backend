package repository;

import models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
  List<Participant> findByUserId(Long userId);
}
