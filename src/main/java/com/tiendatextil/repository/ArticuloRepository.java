package com.tiendatextil.repository;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.Producto;
import com.tiendatextil.model.Talla;
import com.tiendatextil.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    // Define el método de búsqueda por nombre de producto, talla y color
    Optional<Articulo> findByProductoNombreAndTallaTallaAndColorColor(String productoNombre, String talla, String color);
}

