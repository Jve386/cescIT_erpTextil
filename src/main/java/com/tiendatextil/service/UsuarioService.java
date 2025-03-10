package com.tiendatextil.service;

import com.tiendatextil.dto.UsuarioDTO;
import com.tiendatextil.model.Usuario;
import com.tiendatextil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Crear un nuevo usuario
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese correo electrónico.");
        }

        Usuario usuario = convertirAUsuario(usuarioDTO);
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return convertirAUsuarioDTO(nuevoUsuario);
    }

    // Obtener todos los usuarios
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    // Obtener un usuario por su ID
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).map(this::convertirAUsuarioDTO);
    }

    // Actualizar un usuario
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsById(id)) {
            Usuario usuario = convertirAUsuario(usuarioDTO);
            usuario.setId(id);
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return convertirAUsuarioDTO(usuarioActualizado);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Eliminar un usuario
    public void eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Conversión de UsuarioDTO a Usuario
    private Usuario convertirAUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        return usuario;
    }

    // Conversión de Usuario a UsuarioDTO
    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setIdRol(usuario.getRol().getId()); 
        usuarioDTO.setCreatedAt(usuario.getCreatedAt()); 
        return usuarioDTO;
    }
}
