package org.uniremington.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "El nombreUsuario no puede estar vacío")
    private String nombreUsuario;
    @NotBlank(message = "La contrasena no puede estar vacío")
    private String contrasena;
    private Boolean estado;

}