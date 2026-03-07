package com.example.digitalwallet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalwallet.dto.LoginRequest;
import com.example.digitalwallet.dto.RegisterRequest;
import com.example.digitalwallet.dto.UserDto;
import com.example.digitalwallet.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    // =========================
    // TEST API
    // =========================
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Digital Wallet Backend Running");
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            UserDto user = userService.register(request);
            return ResponseEntity.ok(user);
        } 
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {

        UserDto user = userService.login(request);
        return ResponseEntity.ok(user);

    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        

        userService.sendResetPassword(email);

        return ResponseEntity.ok(
            Map.of("message", "Password reset email sent")
        );

    }

    @PostMapping("/reset-password")
public String resetPassword(@RequestParam String token,
@RequestParam String newPassword){

userService.resetPassword(token,newPassword);

return "Password updated successfully";

}

}