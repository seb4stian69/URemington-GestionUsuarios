package org.uniremington.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasena;

    private Boolean estado;

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long idPersona;

    @NotNull(message = "El ID del perfil es obligatorio")
    private Long idPerfil;
}
