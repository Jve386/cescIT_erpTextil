package com.tiendatextil.repository;

import java.util.List;
import com.tiendatextil.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Buscar stock por artículo
    List<Stock> findByArticuloId(Long articuloId);

    // Buscar stock por almacén
    List<Stock> findByAlmacenId(Long almacenId);
}
