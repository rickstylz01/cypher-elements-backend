package com.example.cebackend.seeder;

import com.example.cebackend.models.Event;
import com.example.cebackend.models.Participant;
import com.example.cebackend.models.User;
import com.example.cebackend.repository.EventRepository;
import com.example.cebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDataLoader implements CommandLineRunner {
  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserDataLoader(UserRepository userRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    loadUserData();
  }

  private void loadUserData() {
    if (userRepository.count() == 0) {
      User user1 = new User();
      user1.setUserName("newuser1");
      user1.setEmailAddress("test@example.com");
      user1.setPassword(passwordEncoder.encode("password123"));
      userRepository.save(user1);

      User user2 = new User();
      user2.setUserName("newuser2");
      user2.setEmailAddress("example@test.com");
      user2.setPassword(passwordEncoder.encode("password123"));
      userRepository.save(user2);

      Event event1 = new Event();
      event1.setName("Sox Game");
      LocalDate eventDate = LocalDate.of(2023, 11, 13);
      event1.setEventDate(eventDate);
      event1.setVenue("Sox Park");
      event1.setDescription("Baseball Game!");
      eventRepository.save(event1); // Save the event first

      Event event2 = new Event();
      event2.setName("Bulls Game");
      LocalDate eventDate2 = LocalDate.of(2023, 12, 13);
      event2.setEventDate(eventDate2);
      event2.setVenue("United Center");
      event2.setDescription("Basketball Game!");
      eventRepository.save(event2); // Save the event first

      Event event3 = new Event();
      event3.setName("Budos Band");
      LocalDate eventDate3 = LocalDate.of(2024, 2, 3);
      event3.setEventDate(eventDate3);
      event3.setVenue("Salt Factory");
      event3.setDescription("Music Concert!");
      eventRepository.save(event3); // Save the event first

      // Participants
      List<Participant> participants = new ArrayList<>();

      Participant participant1 = new Participant();
      participant1.setUser(user1);
      participant1.setEvent(event1); // Set the event for participant1
      participants.add(participant1);

      Participant participant2 = new Participant();
      participant2.setUser(user2);
      participant2.setEvent(event1); // Set the event for participant2
      participants.add(participant2);

      event1.getParticipants().addAll(participants);

      eventRepository.save(event1); // Save the event after adding participants
    }
  }
}
