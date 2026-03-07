package com.example.digitalwallet.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalwallet.entity.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByEmail(String email);
    
}
