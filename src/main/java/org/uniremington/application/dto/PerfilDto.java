package org.uniremington.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerfilDto {

    private Long id;
    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;
    private Boolean estado;

}