package org.uniremington.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @Column(name = "idusuario")
    private Long id;

    @Column(name = "nombreu")
    private String nombreUsuario;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "estado")
    private Boolean estado;

    @MapsId
    @OneToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "idPersona")
    private PersonaEntity persona;

    @ManyToOne
    @JoinColumn(name = "idperfil", nullable = false)
    private PerfilEntity perfil;

}