package com.example.wallet.controller;

import com.example.wallet.model.Product;
import com.example.wallet.model.Transaction;
import com.example.wallet.model.User;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import com.example.wallet.repository.ProductRepository;
import com.example.wallet.model.Product;


@RestController
public class WalletController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository txnRepo;

    @Autowired
    private ProductRepository productRepo;


    @PostMapping("/fund")
    public ResponseEntity<?> fundAccount(@RequestBody Map<String, Long> body, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByEmail(username).orElseThrow();

        Long amt = body.get("amt");
        if (amt == null || amt <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Amount must be positive"));
        }

        user.setBalance(user.getBalance() + amt);
        userRepo.save(user);

        txnRepo.save(Transaction.builder()
                .kind("credit")
                .amt(amt)
                .updatedBal(user.getBalance())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build());

        return ResponseEntity.ok(Map.of("balance", user.getBalance()));
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payUser(@RequestBody Map<String, Object> body, Principal principal) {
        String senderEmail = principal.getName();
        User sender = userRepo.findByEmail(senderEmail).orElseThrow();

        String recipientEmail = (String) body.get("to");
        if (recipientEmail == null || recipientEmail.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Recipient email is required"));
        }

        Long amt;
        try {
            amt = Long.valueOf(body.get("amt").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid amount"));
        }

        if (amt <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Amount must be positive"));
        }

        if (sender.getBalance() < amt) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));
        }

        User recipient = userRepo.findByEmail(recipientEmail)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Perform transfer
        sender.setBalance(sender.getBalance() - amt);
        recipient.setBalance(recipient.getBalance() + amt);
        userRepo.save(sender);
        userRepo.save(recipient);

        txnRepo.save(Transaction.builder()
                .kind("debit")
                .amt(amt)
                .updatedBal(sender.getBalance())
                .timestamp(LocalDateTime.now())
                .user(sender)
                .build());

        txnRepo.save(Transaction.builder()
                .kind("credit")
                .amt(amt)
                .updatedBal(recipient.getBalance())
                .timestamp(LocalDateTime.now())
                .user(recipient)
                .build());

        return ResponseEntity.ok(Map.of("balance", sender.getBalance()));
    }
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Principal principal) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(Map.of("balance", user.getBalance()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(Principal principal) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(txnRepo.findByUserOrderByTimestampDesc(user));
}
    @PostMapping("/buy")
    public ResponseEntity<?> buyProduct(@RequestBody Map<String, Long> body, Principal principal) {
        Long productId = body.get("product_id");
        if (productId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Product ID is required"));
        }

        String email = principal.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        Product product = productRepo.findById(productId)
                .orElse(null);

        if (product == null || user.getBalance() < product.getPrice()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient balance or invalid product"));
        }

        // Deduct price
        user.setBalance(user.getBalance() - product.getPrice());
        userRepo.save(user);

    // Save transaction
        txnRepo.save(Transaction.builder()
                .kind("purchase")
                .amt(product.getPrice())
                .updatedBal(user.getBalance())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build());

        return ResponseEntity.ok(Map.of(
                "message", "Product purchased",
                "balance", user.getBalance()
        ));
    }



}
