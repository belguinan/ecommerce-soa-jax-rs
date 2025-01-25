package com.example.userservice.contracts;

import com.example.userservice.database.entities.User;
import com.example.userservice.requests.LoginRequest;

import java.util.Optional;

public interface UserServiceContract {

    public User show();

    public User register(User user);

    public Optional<String> login(LoginRequest loginRequest);

    public boolean logout();

    public User update(User user);
}
