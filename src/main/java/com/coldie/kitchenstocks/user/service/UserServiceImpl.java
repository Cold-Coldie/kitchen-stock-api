package com.coldie.kitchenstocks.user.service;

import com.coldie.kitchenstocks.exception.UserAlreadyExistsException;
import com.coldie.kitchenstocks.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found."));
    }

    @Override
    public User createUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmailEquals(user.getEmail());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email: " + user.getEmail() + " already exists.");
        }

        return userRepository.save(user);
    }


}
