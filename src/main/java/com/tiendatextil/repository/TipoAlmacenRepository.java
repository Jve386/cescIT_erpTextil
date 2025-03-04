package com.tiendatextil.repository;

import com.tiendatextil.model.TipoAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoAlmacenRepository extends JpaRepository<TipoAlmacen, Long> {
    Optional<TipoAlmacen> findByTipo(String tipo);
}
