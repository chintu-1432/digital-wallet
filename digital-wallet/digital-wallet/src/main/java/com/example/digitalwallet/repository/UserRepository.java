package com.example.digitalwallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalwallet.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login & duplicate check)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used during registration validation)
    boolean existsByEmail(String email);

    Optional<User> findByAccountNumber(String accountNumber);
    Optional<User> findByResetToken(String token);
}