package org.uniremington.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import java.util.Date;

@Data
@Entity /*->*/ @Table(name = "personas")
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPersona") /*->*/ private Long id;

    @Column(name = "nom1") /*->*/ private String primerNombre;
    @Column(name = "nom2") /*->*/ private String segundoNombre;
    @Column(name = "apell1") /*->*/ private String primerApellido;
    @Column(name = "apell2") /*->*/ private String segundoApellido;
    @Column(name = "direccion") /*->*/ private String direccion;
    @Column(name = "tele") /*->*/ private String telefono;
    @Column(name = "movil") /*->*/ private String movil;
    @Column(name = "correo") /*->*/ private String correo;
    @Column(name = "fecha_nac") @Temporal(TemporalType.DATE) /*->*/ private Date fechaNacimiento;
    @Column(name = "estado") /*->*/ private String estado;

}

