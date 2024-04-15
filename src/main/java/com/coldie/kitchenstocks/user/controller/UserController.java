package com.coldie.kitchenstocks.user.controller;

import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }
}
