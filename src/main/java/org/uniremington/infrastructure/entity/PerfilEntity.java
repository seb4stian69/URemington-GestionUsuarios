package org.uniremington.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "perfil")
public class PerfilEntity {

    @Id
    @Column(name = "idperfil")
    private Long id;

    @Column(name = "descripc", nullable = false)
    private String descripcion;

    @Column(name = "estado")
    private Boolean estado;

    @OneToMany(mappedBy = "idperfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioEntity> usuarios = new ArrayList<>();

}

