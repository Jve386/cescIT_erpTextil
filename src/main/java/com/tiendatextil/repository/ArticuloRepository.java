package com.tiendatextil.repository;

import com.tiendatextil.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    // Buscar por nombre de producto, talla y color
    Optional<Articulo> findByProductoNombreAndTallaTallaAndColorColor(String productoNombre, String talla, String color);
}

