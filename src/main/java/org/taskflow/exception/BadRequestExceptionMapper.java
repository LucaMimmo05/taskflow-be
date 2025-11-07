package org.taskflow.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        ApiError error = new ApiError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Bad Request",
                exception.getMessage()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }


}
