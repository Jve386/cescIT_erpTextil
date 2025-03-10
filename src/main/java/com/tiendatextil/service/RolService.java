package com.tiendatextil.service;

import com.tiendatextil.model.Rol;
import com.tiendatextil.repository.RolRepository;
import com.tiendatextil.dto.RolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Crear un nuevo rol
    public RolDTO crearRol(RolDTO rolDTO) {
        Rol rol = new Rol(rolDTO.getNombre());
        Rol nuevoRol = rolRepository.save(rol);
        return new RolDTO(nuevoRol.getId(), nuevoRol.getNombre());
    }

    // Obtener todos los roles
    public List<RolDTO> obtenerRoles() {
        return rolRepository.findAll().stream()
            .map(rol -> new RolDTO(rol.getId(), rol.getNombre()))
            .collect(Collectors.toList());
    }

    // Obtener un rol por su ID
    public Optional<RolDTO> obtenerRolPorId(Long id) {
        return rolRepository.findById(id).map(rol -> new RolDTO(rol.getId(), rol.getNombre()));
    }

    // Actualizar un rol
    public RolDTO actualizarRol(Long id, RolDTO rolDTO) {
        if (rolRepository.existsById(id)) {
            Rol rol = new Rol(rolDTO.getNombre());
            rol.setId(id);
            Rol rolActualizado = rolRepository.save(rol);
            return new RolDTO(rolActualizado.getId(), rolActualizado.getNombre());
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
