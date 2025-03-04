package com.tiendatextil.repository;

import com.tiendatextil.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Buscar una categoría por su nombre
    Optional<Categoria> findByNombre(String nombre);
}
