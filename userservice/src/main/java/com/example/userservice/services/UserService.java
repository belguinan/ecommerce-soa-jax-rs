package com.example.userservice.services;

import com.example.userservice.auth.AuthContext;
import com.example.userservice.auth.JwtService;
import com.example.userservice.contracts.UserServiceContract;
import com.example.userservice.database.entities.User;
import com.example.userservice.database.repositories.UserRepository;
import com.example.userservice.requests.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserServiceContract {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthContext authContext;

    /**
     * @param userRepository
     * @param jwtService
     * @param authContext
     */
    public UserService(UserRepository userRepository, JwtService jwtService, AuthContext authContext) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authContext = authContext;
    }

    @Override
    public User show() {
        return this.userRepository.findById(this.authContext.getCurrentUserId()).orElseThrow();
    }

    @Override
    public User register(User user) {
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public Optional<String> login(LoginRequest loginRequest) {
        return this.userRepository.findByUsername(loginRequest.getUsername())
            .filter(user -> this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            .map(this.jwtService::generateToken);
    }

    @Override
    public User update(User user) {
        User dbUser = this.userRepository.findById(this.authContext.getCurrentUserId()).orElseThrow();

        // Prevent user from updating it's username and email.
        dbUser.setUsername(dbUser.getUsername());
        dbUser.setEmail(dbUser.getEmail());
        dbUser.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(dbUser);
    }
}
