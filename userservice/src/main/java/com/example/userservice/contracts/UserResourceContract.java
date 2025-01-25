package com.example.userservice.contracts;

import com.example.userservice.database.entities.User;
import com.example.userservice.requests.LoginRequest;
import com.example.userservice.auth.Authenticated;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/user")
public interface UserResourceContract {

    @POST
    @Path("/register")
    Response register(User user);

    @POST
    @Path("/login")
    Response login(LoginRequest loginRequest);

    @PUT
    @Path("/profile")
    @Authenticated
    Response update(User user);

    @GET
    @Path("/profile")
    @Authenticated
    Response show();
}