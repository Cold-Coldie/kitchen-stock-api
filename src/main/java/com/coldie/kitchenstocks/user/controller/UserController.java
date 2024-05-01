package com.coldie.kitchenstocks.user.controller;

import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        return new ResponseEntity<User>(userService.getUser(), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user) {
        return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long id) {
        return new ResponseEntity<String>(userService.deleteUserById(id), HttpStatus.OK);
    }
}
