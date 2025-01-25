package com.example.userservice.resources;

import com.example.userservice.contracts.UserResourceContract;
import com.example.userservice.contracts.UserServiceContract;
import com.example.userservice.database.entities.User;
import com.example.userservice.requests.LoginRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;

public class UserResource implements UserResourceContract {

    private final UserServiceContract userService;

    @Inject
    public UserResource(UserServiceContract userServiceContract) {
        this.userService = userServiceContract;
    }

    @Override
    public Response register(User user) {
        return Response.ok(this.userService.register(user)).build();
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        String token = this.userService.login(loginRequest).orElseThrow();
        return Response.ok(
            new HashMap<String, Object>() {{
                put("token", token);
            }}
        ).build();
    }

    @Override
    public Response update(User user) {
        return Response.ok(this.userService.update(user)).build();
    }

    @Override
    public Response show() {
        try {
            return Response.ok(this.userService.show()).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return Response.ok().build();
    }
}