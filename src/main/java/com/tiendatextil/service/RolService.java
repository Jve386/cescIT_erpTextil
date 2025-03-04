package com.tiendatextil.service;

import com.tiendatextil.model.Rol;
import com.tiendatextil.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Crear un nuevo rol
    public Rol crearRol(Rol rol) {
        return rolRepository.save(rol);
    }

    // Obtener todos los roles
    public List<Rol> obtenerRoles() {
        return rolRepository.findAll();
    }

    // Obtener un rol por su ID
    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    // Actualizar un rol
    public Rol actualizarRol(Long id, Rol rol) {
        if (rolRepository.existsById(id)) {
            rol.setIdRol(id);  // Aseguramos que el ID no cambie
            return rolRepository.save(rol);
        } else {
            throw new RuntimeException("Rol no encontrado");
        }
    }

    // Eliminar un rol
    public void eliminarRol(Long id) {
        if (rolRepository.existsById(id)) {
            rolRepository.deleteById(id);
        } else {
            throw new RuntimeException("Rol no encontrado");
        }
    }
}
