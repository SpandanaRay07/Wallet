package com.example.wallet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // debit / credit / purchase
    private String kind;

    private Long amt;

    private Long updatedBal;

    private LocalDateTime timestamp;

    // Link transaction to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
