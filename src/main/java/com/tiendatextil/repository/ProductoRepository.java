package com.tiendatextil.repository;

import java.util.List;
import com.tiendatextil.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar un producto por su nombre
    Optional<Producto> findByNombre(String nombre);

    // Buscar productos por categoría
    List<Producto> findByCategoriaId(Long categoriaId);
}
