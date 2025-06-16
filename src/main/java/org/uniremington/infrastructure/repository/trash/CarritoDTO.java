package org.uniremington.infrastructure.repository.trash;

import lombok.Data;
import java.util.List;

@Data
public class CarritoDTO {
    private Long idCarrito;
    private List<DetalleCarrito> productos;
    private Double total;
}
