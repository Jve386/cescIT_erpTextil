package com.tiendatextil.repository;

import java.util.List;


import com.tiendatextil.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    // Buscar detalles de ventas por venta
    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.articulo a JOIN FETCH a.producto JOIN FETCH a.talla")
    List<DetalleVenta> findAllWithDetails();
}
