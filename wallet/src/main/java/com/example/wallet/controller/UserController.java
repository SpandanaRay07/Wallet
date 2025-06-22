package com.example.wallet.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.wallet.model.User;
import com.example.wallet.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        // ✅ Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(0L); // Optional: set initial balance

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
