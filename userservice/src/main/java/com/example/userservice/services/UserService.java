package com.example.userservice.services;

import com.example.userservice.auth.AuthContext;
import com.example.userservice.auth.JerseyRequestContextFilter;
import com.example.userservice.auth.JwtService;
import com.example.userservice.contracts.UserServiceContract;
import com.example.userservice.database.entities.User;
import com.example.userservice.database.repositories.UserRepository;
import com.example.userservice.requests.LoginRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public Optional<HashMap> login(LoginRequest loginRequest) {
        return this.userRepository.findByUsername(loginRequest.getUsername())
            .filter(user -> this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            .map(user -> {
                String token = this.jwtService.generateToken(user);
                return new HashMap<String, Object>() {{
                    put("token", token);
                    put("user", user);
                }};
            });
    }

    public boolean logout() {
        var requestContext = JerseyRequestContextFilter.getCurrentContext();
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring("Bearer ".length()).trim();
        this.jwtService.invalidateToken(token);
        return true;
    }

    @Override
    public User update(User user) {
        User dbUser = this.userRepository.findById(this.authContext.getCurrentUserId()).orElseThrow();

        // Fillable columns
        dbUser.setName(user.getName());
        dbUser.setAddress(user.getAddress());
        dbUser.setPhoneNumber(user.getPhoneNumber());
        dbUser.setCity(user.getCity());
        dbUser.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // Prevent user from updating it's username and email.
        dbUser.setUsername(dbUser.getUsername());
        dbUser.setEmail(dbUser.getEmail());

        return this.userRepository.save(dbUser);
    }
}
