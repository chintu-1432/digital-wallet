package com.example.digitalwallet.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 150, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hash

    @Builder.Default
    @Column(nullable = false)
    private Double balance = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private String role = "USER"; // USER or ADMIN
    
    @Column(unique = true)
    private String  accountNumber;
    @Column
    private String resetToken;
    @Column
    private LocalDateTime resetTokenExpiry;
}