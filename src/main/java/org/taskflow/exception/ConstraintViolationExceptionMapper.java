package org.taskflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
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

        String fieldName = extractFieldName(violation);

        error.put("field", fieldName);
        error.put("message", violation.getMessage());

        return error;
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        // Iterate over the Path nodes and return the last non-null node name.
        // This avoids exposing parameter/method/class prefixes like "register.userRequest.email".
        String lastName = null;
        for (Path.Node node : violation.getPropertyPath()) {
            if (node.getName() != null && !node.getName().isEmpty()) {
                lastName = node.getName();
            }
        }

        if (lastName != null) {
            return lastName;
        }

        // Fallback to the full path string if node names are not available
        String fullPath = violation.getPropertyPath().toString();
        // Try a final attempt to extract after dots as a safety net
        String[] parts = fullPath.split("\\.");
        if (parts.length >= 1) {
            return parts[parts.length - 1];
        }

        return fullPath;
    }
}
