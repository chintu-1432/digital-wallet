package com.example.digitalwallet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.entity.User;
import com.example.digitalwallet.repository.TransactionRepository;
import com.example.digitalwallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // Total Users
    @GetMapping("/users/count")
    public long getUserCount(){
        return userRepository.count();
    }

    // Total Transactions
    @GetMapping("/transactions/count")
    public long getTransactionCount(){
        return transactionRepository.count();
    }

    // All Users
    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // All Transactions
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

}