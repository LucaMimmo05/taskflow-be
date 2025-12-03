package org.taskflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Map<String, String>> violations = exception.getConstraintViolations()
                .stream()
                .map(this::mapViolation)
                .collect(Collectors.toList());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("title", "Constraint Violation");
        errorResponse.put("status", 400);
        errorResponse.put("violations", violations);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    private Map<String, String> mapViolation(ConstraintViolation<?> violation) {
        Map<String, String> error = new HashMap<>();

        String fullPath = violation.getPropertyPath().toString();
        String fieldName = extractFieldName(fullPath);

        error.put("field", fieldName);
        error.put("message", violation.getMessage());

        return error;
    }

    private String extractFieldName(String fullPath) {
        String[] parts = fullPath.split("\\.");

        if (parts.length >= 2) {
            return parts[parts.length - 1];
        }

        return fullPath;
    }
}

