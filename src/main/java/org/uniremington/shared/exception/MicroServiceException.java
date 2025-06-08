package org.uniremington.shared.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uniremington.shared.base.BaseError;

import java.time.Instant;
import java.util.Date;

@Data/**/@NoArgsConstructor/**/@AllArgsConstructor
public abstract class MicroServiceException extends RuntimeException {

    private int codigoHttp;
    private String origen;
    private String mensaje;

    public BaseError getBaseError() {
        return new BaseError(origen, mensaje, Date.from(Instant.now()).toString());
    }

    public static BaseError procesar(Exception ex, String origen) {
        return new BaseError(origen, ex.getMessage(), Date.from(Instant.now()).toString());
    }

}
