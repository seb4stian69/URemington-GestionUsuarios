package org.uniremington.infrastructure.repository.trash;

public class AgregarProductoRequest {
    private Long idConsumidor;
    private String idProducto;
    private int cantidad;

    // Getters y setters
    public Long getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(Long idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
