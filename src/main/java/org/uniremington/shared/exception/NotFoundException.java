package org.uniremington.shared.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends MicroServiceException {
    public NotFoundException(String message, String origen) {
        super(Response.Status.NOT_FOUND.getStatusCode(), origen, message);
    }
}