package com.example.wallet.controller;

import com.example.wallet.model.Product;
import com.example.wallet.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;
   @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        Product saved = productRepo.save(product);

        return ResponseEntity
                .status(201)
                .body(Map.of(
                    "id", saved.getId(),
                    "message", "Product added"
                ));
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepo.findAll());
    }
}
