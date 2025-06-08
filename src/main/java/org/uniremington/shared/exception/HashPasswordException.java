package org.uniremington.shared.exception;

import jakarta.ws.rs.core.Response;

public class HashPasswordException extends MicroServiceException {
    public HashPasswordException(String message, String origen) {
        super(Response.Status.BAD_REQUEST.getStatusCode(), origen, message);
    }
}