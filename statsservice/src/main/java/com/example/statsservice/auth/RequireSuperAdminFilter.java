package com.example.statsservice.auth;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class RequireSuperAdminFilter implements ContainerRequestFilter {

    private final AuthContext context;

    @Inject
    public RequireSuperAdminFilter(AuthContext context) {
        this.context = context;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (! this.context.isSuperAdmin()) {
            throw new NotAuthorizedException("Not enough privileges");
        }
    }
}