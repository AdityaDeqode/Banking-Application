package com.example.BankingApplication.service;

import com.example.BankingApplication.Repository.UserRepository;
import com.example.BankingApplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String loginUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            double balance = existingUser.getBalance();
            return "Login successful. Current balance: " + balance;
        } else {
            return "Invalid credentials";
        }
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


}
