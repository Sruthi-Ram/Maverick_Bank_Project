package com.hexaware.maverickBank.controller;

import java.util.NoSuchElementException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.service.interfaces.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User loginUser(@RequestParam String identifier, @RequestParam String password) {
        User user = userService.loginUser(identifier, password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return user;
    }

    @GetMapping("/getuserbyid/{userId}")
    public User getUserById(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        return user;
    }

    @PutMapping("/updateuser/{userId}")
    public User updateUser(@PathVariable int userId,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String role,
                             @RequestParam(required = false) String status) {
        User updatedUser = userService.updateUser(userId, password, email, role, status);
        if (updatedUser == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        return updatedUser;
    }

    @DeleteMapping("/deactivateuser/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        userService.deactivateUser(userId);
    }

    @DeleteMapping("/delete/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        userService.deleteUser(userId);
    }
}