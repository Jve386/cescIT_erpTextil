package com.tiendatextil.repository;

import com.tiendatextil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí puedes añadir consultas personalizadas si es necesario
}
