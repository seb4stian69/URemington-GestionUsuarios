package org.uniremington.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@RegisterForReflection
@Data/**/@Getter@Setter
public class PersonaDto {
    private Long id;

    private Long cedula;

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

    private Boolean estado;
}