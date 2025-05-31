package org.uniremington.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.uniremington.shared.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class Usuario extends BaseEntity {
    private Long id;
    private String nombreUsuario;
    private String contrasena;
}