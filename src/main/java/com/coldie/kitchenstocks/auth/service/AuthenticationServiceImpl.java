package com.coldie.kitchenstocks.auth.service;

import com.coldie.kitchenstocks.auth.request.AuthenticationRequest;
import com.coldie.kitchenstocks.auth.request.RegisterRequest;
import com.coldie.kitchenstocks.auth.response.AuthenticationResponse;
import com.coldie.kitchenstocks.config.JwtService;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.token.model.Token;
import com.coldie.kitchenstocks.token.repository.TokenRepository;
import com.coldie.kitchenstocks.token.type.TokenType;
import com.coldie.kitchenstocks.user.exception.UserAlreadyExistsException;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import com.coldie.kitchenstocks.user.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        try {
            Optional<User> optionalUser = userRepository.findByEmailEquals(request.getEmail());

            if (optionalUser.isPresent()) throw new UserAlreadyExistsException("User with this email already exists.");

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setCurrency(request.getCurrency());
            user.setCountry(request.getCountry());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);

            user = userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);

            saveUserToken(user, jwtToken);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwtToken);

            return authenticationResponse;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

            User user = userRepository.findByEmailEquals(request.getEmail()).orElseThrow(() ->
                    new UserNotFoundException("User with this email does not exist.")
            );

            String jwtToken = jwtService.generateToken(user);

            revokeAllUserTokens(user);

            saveUserToken(user, jwtToken);

            AuthenticationResponse authResponse = new AuthenticationResponse();
            authResponse.setToken(jwtToken);

            return authResponse;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred");
        }
    }

    public void saveUserToken(User user, String jwtToken) {
        Token token = new Token();

        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        } else {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });

            tokenRepository.saveAll(validUserTokens);
        }
    }

}
