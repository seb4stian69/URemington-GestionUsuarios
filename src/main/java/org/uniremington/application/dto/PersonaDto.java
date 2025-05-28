package org.uniremington.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class PersonaDto {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombres;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellidos;

    private String direccion;
    private String telefono;
    private String movil;

    @Email(message = "Correo no válido")
    private String correo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private Date fechaNacimiento;

    private String estado;
}