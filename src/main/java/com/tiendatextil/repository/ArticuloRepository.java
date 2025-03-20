package com.tiendatextil.repository;

import com.tiendatextil.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    Optional<Articulo> findByProductoNombreAndTallaTallaAndColorColor(String nombreProducto, String talla, String color);
}

