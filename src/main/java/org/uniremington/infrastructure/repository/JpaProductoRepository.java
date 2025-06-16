package org.uniremington.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import org.uniremington.infrastructure.entity.ProductoEntity;
import org.uniremington.infrastructure.repository.trash.CarritoDTO;
import org.uniremington.infrastructure.repository.trash.DetalleCarrito;
import org.uniremington.shared.exception.RepoException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaProductoRepository {

    @PersistenceContext EntityManager em;

    @Inject JpaProductoRepository(EntityManager em){
        this.em = em;
    }

    @Transactional
    public ProductoEntity save(ProductoEntity producto) {
        em.persist(producto);
        return producto;
    }

    @Transactional
    public ProductoEntity update(ProductoEntity producto) {
        return em.merge(producto);
    }

    @Transactional
    public void delete(String codprod) {
        ProductoEntity producto = em.find(ProductoEntity.class, codprod);
        producto.setEstado(false);
        em.merge(producto);
    }

    public Optional<ProductoEntity> findById(String codprod) {
        ProductoEntity producto = em.find(ProductoEntity.class, codprod);
        return Optional.ofNullable(producto);
    }

    public List<ProductoEntity> findAll(Long idProd) {
        return em.createQuery("SELECT p FROM ProductoEntity p WHERE idProductor = :idP", ProductoEntity.class)
                .setParameter("idP", idProd)
                 .getResultList();
    }

    public CarritoDTO obtenerCarritoConDetalles(Long idUser) {

        Long idCar = (Long) em.createNativeQuery("SELECT idCarrito FROM carrito WHERE idConsumidor = ?")
                .setParameter(1, idUser)
                .getSingleResult();

        Object[] result = (Object[]) em.createNativeQuery("CALL obtener_carrito_con_detalles(:idCarrito)")
                .setParameter("idCarrito", idCar)
                .getSingleResult();

        Long id = ((Number) result[0]).longValue();
        String productosJson = (String) result[1];
        Double total = ((Number) result[2]).doubleValue();

        ObjectMapper mapper = new ObjectMapper();
        List<DetalleCarrito> productos;

        try {
            productos = mapper.readValue(productosJson, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RepoException("Error al deserializar productos del carrito", JpaProductoRepository.class.getSimpleName());
        }

        CarritoDTO dto = new CarritoDTO();
        dto.setIdCarrito(id);
        dto.setProductos(productos);
        dto.setTotal(total);

        return dto;

    }

    @Transactional
    public void agregarProductoAlCarrito(Long idConsumidor, String idProducto, int cantidad) {
        try {
            em.createNativeQuery("CALL agregar_producto_al_carrito(:idConsumidor, :idProducto, :cantidad)")
                    .setParameter("idConsumidor", idConsumidor)
                    .setParameter("idProducto", idProducto)
                    .setParameter("cantidad", cantidad)
                    .executeUpdate();
        } catch (Exception e) {
            throw new RepoException("Error al agregar producto al carrito: " + e.getMessage(),
                    JpaProductoRepository.class.getSimpleName());
        }
    }



}
