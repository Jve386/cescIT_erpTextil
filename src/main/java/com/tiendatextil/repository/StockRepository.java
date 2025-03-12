package com.tiendatextil.repository;

import java.util.List;
import java.util.Optional;

import com.tiendatextil.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Buscar stock por artículo
    List<Stock> findByArticuloId(Long articuloId);

    // Buscar stock por artículo y almacén (usando la notación correcta de JPA)
    Optional<Stock> findByArticulo_IdAndAlmacen_Id(Long articuloId, Long almacenId);
}
