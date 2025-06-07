package org.uniremington.shared.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data/**/@NoArgsConstructor/**/@AllArgsConstructor
public class BaseError {
    private String origen;
    private String detalle;
    private String fechaYHora;
}
