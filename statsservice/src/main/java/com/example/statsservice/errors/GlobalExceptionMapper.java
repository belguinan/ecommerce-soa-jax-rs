package com.example.statsservice.errors;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof NotFoundException) {
            return buildErrorResponse("Resource not found", 404);
        }

        if (exception instanceof NotAuthorizedException) {
            return buildErrorResponse("Unauthorized", 401);
        }

        if (exception instanceof IllegalArgumentException || exception instanceof BadRequestException) {
            return buildErrorResponse(exception.getMessage(), 400);
        }

        log.error(exception.getMessage(), exception);

        return buildErrorResponse("Internal server error", 500);
    }

    /**
     * @param message
     * @param status
     * @return
     */
    private Response buildErrorResponse(String message, int status) {
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                new HashMap<>(),
                status
        );

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}