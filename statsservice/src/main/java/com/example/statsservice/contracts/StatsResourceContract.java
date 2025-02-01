package com.example.statsservice.contracts;

import com.example.statsservice.auth.Authenticated;
import com.example.statsservice.auth.RequireSuperAdmin;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@RequireSuperAdmin
@Path("/stats")
public interface StatsResourceContract {

    @GET
    @Path("/dashboard")
    Response dashboard(
        @QueryParam("interval") @Pattern(regexp = "^(day|week|month)$", message = "Invalid interval value.") String interval
    );

    @GET
    @Path("/sales")
    Response sales(
        @QueryParam("interval") @Pattern(regexp = "^(day|week|month)$", message = "Invalid interval value.") String interval
    );

    @GET
    @Path("/products")
    Response products(
        @QueryParam("interval") @Pattern(regexp = "^(day|week|month)$", message = "Invalid interval value.") String interval
    );

    @GET
    @Path("/users")
    Response users(
        @QueryParam("interval") @Pattern(regexp = "^(day|week|month)$", message = "Invalid interval value.") String interval
    );
}