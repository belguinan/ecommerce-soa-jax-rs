package com.example.userservice.contracts;

import com.example.userservice.database.entities.User;
import com.example.userservice.requests.LoginRequest;

import java.util.HashMap;
import java.util.Optional;

public interface UserServiceContract {

    /**
     * @return User
     */
    public User show();

    /**
     * @param user
     * @return User
     */
    public User register(User user);

    /**
     * @param loginRequest
     * @return Optional<String>
     */
    public Optional<HashMap> login(LoginRequest loginRequest);

    /**
     * @param user
     * @return User
     */
    public User update(User user);

    /**
     * @return boolean
     */
    public boolean logout();

    /**
     * @return boolean
     */
    public boolean isLoggedIn();
}
