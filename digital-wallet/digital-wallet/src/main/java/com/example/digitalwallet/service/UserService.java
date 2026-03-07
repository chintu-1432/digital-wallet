package com.example.digitalwallet.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.digitalwallet.dto.LoginRequest;
import com.example.digitalwallet.dto.RegisterRequest;
import com.example.digitalwallet.dto.UserDto;
import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.entity.User;
import com.example.digitalwallet.repository.TransactionRepository;
import com.example.digitalwallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
private final EmailService emailService;
    


    private String generateAccountNumber(){
    // Simple unique account number generation
    long accountNumber = 1000000000L + new java.util.Random().nextInt(90000000);
    return String.valueOf(accountNumber);
    }
    // =========================
    // REGISTER
    // =========================
    public UserDto register(RegisterRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already registered");
    }
        // Simple unique account number generation
    String accountNumber = generateAccountNumber();
    User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .balance(0.0)
            .accountNumber(accountNumber)
            .role("USER")
            .build();

    User saved = userRepository.save(user);

    return UserDto.builder()
            .id(saved.getId())
            .name(saved.getName())
            .email(saved.getEmail())
            .balance(saved.getBalance())
            .accountNumber(saved.getAccountNumber())
            .build();


                 
}

    // =========================
    // LOGIN
    // =========================
    public UserDto login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    return UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .balance(user.getBalance())
            .accountNumber(user.getAccountNumber())
            .build();
}

    // =========================
    // GET BALANCE
    // =========================
    public Double getBalance(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getBalance();
    }

    // =========================
    // DEPOSIT
    // =========================
    public Double deposit(String email, Double amount) {

        if(amount <= 0) {
            throw new RuntimeException("invalid amount");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction tx = Transaction.builder()
                .email(email)
                .type("DEPOSIT")
                .amount(amount)
                .date(java.time.LocalDateTime.now())
                .build();
        transactionRepository.save(tx);

        return user.getBalance();
    }

    // =========================
    // WITHDRAW
    // =========================
    public Double withdraw(String email, Double amount) {
        if(amount <= 0) {
            throw new RuntimeException("invalid amount");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                if(user.getBalance() < amount) {
                    throw new RuntimeException("Insufficient balance");
                }
                user.setBalance(user.getBalance() - amount);
                userRepository.save(user);
                Transaction tx = Transaction.builder()
                        .email(email)
                        .type("WITHDRAWAL")
                        .amount(amount)
                        .date(java.time.LocalDateTime.now())
                        .build();
                transactionRepository.save(tx);
        return user.getBalance();
    }

    public Double transfer(String fromEmail, String toEmail, Double amount) {

    if(amount <= 0){
        throw new RuntimeException("Invalid amount");
    }

    if(fromEmail.equals(toEmail)){
        throw new RuntimeException("Cannot transfer to same account");
    }

    User sender = userRepository.findByEmail(fromEmail)
            .orElseThrow(() -> new RuntimeException("Sender not found"));

    User receiver = userRepository.findByEmail(toEmail)
            .orElseThrow(() -> new RuntimeException("Receiver not found"));

    if(sender.getBalance() < amount){
        throw new RuntimeException("Insufficient balance");
    }

    sender.setBalance(sender.getBalance() - amount);
    receiver.setBalance(receiver.getBalance() + amount);

    userRepository.save(sender);
    userRepository.save(receiver);

    return sender.getBalance();
}

public void sendResetPassword(String email){

User user=userRepository.findByEmail(email)
.orElseThrow(() -> new RuntimeException("User not found"));

String token = java.util.UUID.randomUUID().toString();

user.setResetToken(token);
user.setResetTokenExpiry(java.time.LocalDateTime.now().plusMinutes(30));

userRepository.save(user);

String resetLink =
"http://localhost:8080/reset.html?token=" + token;

emailService.sendMail(
email,
"Wallet Password Reset",
"Click the link to reset password:\n" + resetLink
);

}

public void resetPassword(String token,String newPassword){

User user=userRepository.findByResetToken(token)
.orElseThrow(() -> new RuntimeException("Invalid token"));

if(user.getResetTokenExpiry().isBefore(java.time.LocalDateTime.now())){
throw new RuntimeException("Token expired");
}

user.setPassword(passwordEncoder.encode(newPassword));

user.setResetToken(null);
user.setResetTokenExpiry(null);

userRepository.save(user);

}
}