package com.example.userservice.contracts;

import com.example.userservice.database.entities.User;
import com.example.userservice.requests.LoginRequest;

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
    public Optional<String> login(LoginRequest loginRequest);

    /**
     * @return boolean
     */
    public boolean logout();

    /**
     * @param user
     * @return User
     */
    public User update(User user);
}
