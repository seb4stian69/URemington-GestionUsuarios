package org.uniremington.shared.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends MicroServiceException {
    public NotFoundException(String message, String origen) {
        super(Response.Status.BAD_REQUEST.getStatusCode(), origen, message);
    }
}