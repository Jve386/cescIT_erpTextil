package com.tiendatextil.repository;

import com.tiendatextil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Buscar todos los usuarios con un rol específico
    List<Usuario> findByRolId(Long rolId);
}
