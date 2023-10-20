package com.example.cebackend.repository;

import com.example.cebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByEmailAddress(String emailAddress);
}
