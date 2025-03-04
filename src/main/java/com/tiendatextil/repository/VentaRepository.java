package com.tiendatextil.repository;

import com.tiendatextil.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Buscar ventas por cliente
    List<Venta> findByClienteId(Long clienteId);

    // Buscar ventas por almacén
    List<Venta> findByAlmacenId(Long almacenId);

    // Buscar ventas dentro de un rango de fechas
    List<Venta> findByFechaBetween(Date fechaInicio, Date fechaFin);
}
