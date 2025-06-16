package org.uniremington.infrastructure.repository.trash;

import lombok.Data;

@Data
public class DetalleCarrito {
    private Long idProducto;
    private String nombre;
    private int cantidad;
    private double precio_unitario;
    private double subtotal;
    private String urlImage;
}
