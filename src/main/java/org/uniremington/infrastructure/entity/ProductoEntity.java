package org.uniremington.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class ProductoEntity {

    @Id
    @Column(name = "codprod")
    private String codprod; // Si no es autogenerado y es tipo texto

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio_base")
    private Double precioBase;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "foto")
    private String foto;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "practicas_cultivo")
    private String practicasCultivo;

    @Column(name = "disponibilidad")
    private String disponibilidad;

    @Column(name = "porc_descuento")
    private Double porcDescuento;

    @Column(name = "idcategoria")
    private Long idCategoria;

    @Column(name = "idproductor")
    private Long idProductor;
}


