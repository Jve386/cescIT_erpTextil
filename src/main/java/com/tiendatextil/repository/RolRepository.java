package com.tiendatextil.repository;

import com.tiendatextil.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Buscar un rol por su nombre
    Optional<Rol> findByNombre(String nombre);
}
