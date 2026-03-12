package com.example.digitalwallet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalwallet.dto.TransferRequest;
import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.repository.TransactionRepository;
import com.example.digitalwallet.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final UserService userService;
    private final TransactionRepository transactionRepository;

    // Get Balance
    @GetMapping("/balance/{email}")
    public Double getBalance(@PathVariable String email) {
        return userService.getBalance(email);
    }

    // Deposit
    @PostMapping("/deposit")
    public Double deposit(@RequestParam String email,
                          @RequestParam Double amount) {
        return userService.deposit(email, amount);
    }

    // Withdraw
    @PostMapping("/withdraw")
    public Double withdraw(@RequestParam String email,
                           @RequestParam Double amount) {
        return userService.withdraw(email, amount);
    }

    // Transaction History
    @GetMapping("/transactions/{email}")
    public List<Transaction> getTransactions(@PathVariable String email) {
        return transactionRepository.findByEmail(email);
    }

    @PostMapping("/transfer")
public Double transfer(@RequestBody TransferRequest request) {

    return userService.transfer(
            request.getFromEmail(),
            request.getToEmail(),
            request.getAmount()
    );
}

}

