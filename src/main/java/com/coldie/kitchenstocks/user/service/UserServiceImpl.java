package com.coldie.kitchenstocks.user.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.user.exception.UserAlreadyExistsException;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser() {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return userRepository.findByEmailEquals(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            User savedUser = userRepository.findByEmailEquals(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            if (Objects.nonNull(user.getFirstName())) {
                savedUser.setFirstName(user.getFirstName());
            }
            if (Objects.nonNull(user.getLastName())) {
                savedUser.setLastName(user.getLastName());
            }
            if (Objects.nonNull(user.getCountry())) {
                savedUser.setCountry(user.getCountry());
            }
            if (Objects.nonNull(user.getCurrency())) {
                savedUser.setCurrency(user.getCurrency());
            }
            if (Objects.nonNull(user.getEmail())) {
                savedUser.setEmail(user.getEmail());
            }
            if (Objects.nonNull(user.getPassword())) {
                savedUser.setPassword(user.getPassword());
            }

            return userRepository.save(savedUser);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }
}
