package com.example.BankingApplication.controller;


import com.example.BankingApplication.model.Transaction;
import com.example.BankingApplication.model.User;
import com.example.BankingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class myController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/login" , produces = "application/json")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/deposit")
    public String depositBalance(@RequestBody User user) {
        return userService.depositBalance(user);
    }

    @PostMapping("/withdraw")
    public String withdrawBalance(@RequestBody User user) {
        return userService.withdrawBalance(user);
    }

    @GetMapping("/{username}/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactionsForUser(@PathVariable String username) {
        List<Transaction> transactions = userService.getAllTransactionsForUser(username);
        return ResponseEntity.ok(transactions);
    }





}
