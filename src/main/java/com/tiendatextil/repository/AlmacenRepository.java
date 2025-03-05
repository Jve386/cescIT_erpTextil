package com.tiendatextil.repository;

import com.tiendatextil.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    // Buscar almacén por nombre
    Optional<Almacen> findByNombre(String nombre);

    // Buscar almacenes por tipo
    List<Almacen> findByTipoAlmacen(String tipoAlmacen);
}