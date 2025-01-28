package com.example.cartservice.contracts;

import com.example.cartservice.auth.Authenticated;
import com.example.cartservice.database.entities.OrderItem;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Path("/cart")
public interface CartResourceContract {

    @GET
    @Path("/")
    Response index();

    @POST
    @Path("/{id}")
    Response store(@PathParam("id") Long id, OrderItem orderItem) throws Exception;

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, OrderItem orderItem) throws Exception;

    @DELETE
    @Path("/{id}")
    Response destroy(@PathParam("id") Long id);
}