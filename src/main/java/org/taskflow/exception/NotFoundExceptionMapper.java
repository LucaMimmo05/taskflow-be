package org.taskflow.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        ApiError error = new ApiError(
                Response.Status.NOT_FOUND.getStatusCode(),
                "Not Found",
                e.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}
