package com.example.productservice.contracts;

import com.example.productservice.database.entities.Product;
import com.example.productservice.auth.Authenticated;
import com.example.productservice.pagination.PaginationParams;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Path("/product")
public interface ProductResourceContract {

    @GET
    @Path("/")
    Response index(
        @QueryParam("page") @DefaultValue("1") int page,
        @QueryParam("perPage") @DefaultValue("15") int perPage,
        @QueryParam("sortBy") @DefaultValue("id") @Pattern(regexp = "^(id|price|stock|name)$", message = "Invalid sort by param.") String sortBy,
        @QueryParam("sortOrder") @DefaultValue("DESC") String sortOrder,
        @QueryParam("type") String type,
        @QueryParam("sellerId") String sellerId
    );

    @POST
    @Path("/")
    Response store(Product product);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, Product product);

    @GET
    @Path("/{id}")
    Response show(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    Response destroy(@PathParam("id") Long id);
}