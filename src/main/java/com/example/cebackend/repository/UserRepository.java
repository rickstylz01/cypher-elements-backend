package com.example.cebackend.repository;

import com.example.cebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmailAddress(String emailAddress);
  User findUserByEmailAddress(String emailAddress);
}
