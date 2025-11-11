package org.taskflow.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.io.InputStream;

@Provider
public class JaxRsNotFoundPageMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        InputStream page = getClass().getResourceAsStream("/META-INF/resources/404.html");

        if (page == null) {
            String fallback = "<html><h1>404 - Not Found</h1><p>Page not found</p></html>";
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(fallback)
                    .type("text/html")
                    .build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity(page)
                .type("text/html")
                .build();
    }
}
