package com.example.BankingApplication.service;

import com.example.BankingApplication.Repository.UserRepository;
import com.example.BankingApplication.model.Transaction;
import com.example.BankingApplication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?>  loginUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            double balance = existingUser.getBalance();

            String token = generateJWTToken(existingUser.getUsername());
            JwtResponse response = new JwtResponse(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private String generateJWTToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);

        Date expiration = new Date(System.currentTimeMillis() + 86400000); // 24 hours
        claims.setExpiration(expiration);

        // Sign the token with a secret key
        String secretKey = "your-secret-key";
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    public String registerUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "Username already exists";
        } else {
            userRepository.save(user);
            return "Registration successful";
        }
    }

    public String depositBalance(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {

            double currentBalance = existingUser.getBalance();
            double amount = user.getBalance();
            double balance = currentBalance + amount;
            recordDepositTransaction(existingUser, amount, balance);
            existingUser.setBalance(currentBalance + amount);
            userRepository.save(existingUser);
            return "Deposit successful. Current balance: " + existingUser.getBalance();
        } else {
            return "User not found";
        }
    }

    public String withdrawBalance(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            double currentBalance = existingUser.getBalance();
            double amount = user.getBalance();
            if (currentBalance >= amount) {
                double balance = currentBalance - amount;
                recordWithdrawalTransaction(existingUser, amount, balance);
                existingUser.setBalance(currentBalance - amount);
                userRepository.save(existingUser);
                return "Withdrawal successful. Current balance: " + existingUser.getBalance();
            } else {
                return "Insufficient balance";
            }
        } else {
            return "User not found";
        }
    }

    public List<Transaction> getAllTransactionsForUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getTransactions();
        } else {
            return Collections.emptyList();
        }
    }


    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }


    public String recordDepositTransaction(User user, double amount, double balance) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            Transaction transaction = new Transaction();
            transaction.setType("Deposit");
            transaction.setAmount(amount);
            transaction.setBalance(balance);
            transaction.setUser(existingUser);
            existingUser.getTransactions().add(transaction);
            userRepository.save(existingUser);
            return "Deposit transaction recorded";
        } else {
            return "User not found";
        }
    }

    public String recordWithdrawalTransaction(User user, double amount, double balance) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            Transaction transaction = new Transaction();
            transaction.setType("Withdrawal");
            transaction.setAmount(amount);
            transaction.setBalance(balance);
            transaction.setUser(existingUser);
            existingUser.getTransactions().add(transaction);
            userRepository.save(existingUser);
            return "Withdrawal transaction recorded";
        } else {
            return "User not found";
        }
    }
}
