package com.example.orderservice.contracts;

import com.example.orderservice.auth.Authenticated;
import com.example.orderservice.database.entities.Order;
import com.example.orderservice.database.entities.OrderItem;
import com.example.orderservice.requests.CheckoutRequest;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Path("/order")
public interface OrderResourceContract {
    @GET
    @Path("/")
    Response index(
        @QueryParam("page") @DefaultValue("1") int page,
        @QueryParam("perPage") @DefaultValue("15") int perPage,
        @QueryParam("sortBy") @DefaultValue("id") @Pattern(regexp = "^(id|total)$", message = "Invalid sort by param.") String sortBy,
        @QueryParam("sortOrder") @DefaultValue("DESC") String sortOrder
    );

    @GET
    @Path("/{id}")
    Response show(@PathParam("id") Long id);

    @POST
    @Path("/checkout")
    Response checkout(CheckoutRequest request);
}