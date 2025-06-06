package org.uniremington.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.uniremington.shared.base.BaseEntity;

@Data/**/@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Usuario extends BaseEntity {
    private Long id;
    private String nombreUsuario;
    private String contrasena;
    private String idPerfil;
    private String salt;
    private Boolean estado;
}