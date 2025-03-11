package com.tiendatextil.repository;

import java.util.List;
import java.util.Optional;

import com.tiendatextil.model.Almacen;
import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Buscar stock por artículo
    List<Stock> findByArticuloId(Long articuloId);

    // Buscar stock por almacén
    Optional<Stock> findByAlmacen_IdAndArticulo_Id(Long almacenId, Long articuloId);

    // Buscar stock por artículo y almacén
    Optional<Stock> findByAlmacenIdAndArticuloId(Long almacenId, Long articuloId);


}
