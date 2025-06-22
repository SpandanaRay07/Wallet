package com.example.wallet.repository;

import com.example.wallet.model.Transaction;
import com.example.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    
}
