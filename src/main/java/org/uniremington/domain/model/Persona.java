package org.uniremington.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.uniremington.shared.base.BaseEntity;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Persona extends BaseEntity {
    private Long id;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String movil;
    private String correo;
    private Date fechaNacimiento;
}
