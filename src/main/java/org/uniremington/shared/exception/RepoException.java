package org.uniremington.shared.exception;

import jakarta.ws.rs.core.Response;

public class RepoException extends MicroServiceException {
    public RepoException(String message, String origen) {
        super(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), origen, message);
    }
}