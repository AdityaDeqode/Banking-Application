package com.example.BankingApplication.controller;


import com.example.BankingApplication.Repository.UserRepository;
import com.example.BankingApplication.model.User;
import com.example.BankingApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class myController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
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





}
