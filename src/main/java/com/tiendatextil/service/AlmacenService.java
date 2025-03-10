package com.tiendatextil.service;

import com.tiendatextil.dto.AlmacenDTO;
import com.tiendatextil.model.Almacen;
import com.tiendatextil.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    @Autowired
    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Crear un nuevo almacén
    public AlmacenDTO crearAlmacen(AlmacenDTO almacenDTO) {
        if (almacenDTO.getTipoAlmacen() == null || 
            (!almacenDTO.getTipoAlmacen().equals("Tienda") && 
             !almacenDTO.getTipoAlmacen().equals("Almacén"))) {
            throw new IllegalArgumentException("Tipo de almacén inválido");
        }

        Almacen almacen = convertirAAlmacen(almacenDTO);
        Almacen almacenGuardado = almacenRepository.save(almacen);
        return convertirAAlmacenDTO(almacenGuardado);
    }

    // Obtener todos los almacenes
    public List<AlmacenDTO> obtenerAlmacenes() {
        List<Almacen> almacenes = almacenRepository.findAll();
        return almacenes.stream().map(this::convertirAAlmacenDTO).collect(Collectors.toList());
    }

    // Obtener un almacén por su ID
    public Optional<AlmacenDTO> obtenerAlmacenPorId(Long id) {
        return almacenRepository.findById(id).map(this::convertirAAlmacenDTO);
    }

    // Actualizar un almacén
    public AlmacenDTO actualizarAlmacen(Long id, AlmacenDTO almacenDTO) {
        if (almacenDTO.getTipoAlmacen() == null || 
            (!almacenDTO.getTipoAlmacen().equals("Tienda") && 
             !almacenDTO.getTipoAlmacen().equals("Almacén"))) {
            throw new IllegalArgumentException("Tipo de almacén inválido");
        }

        if (almacenRepository.existsById(id)) {
            Almacen almacen = convertirAAlmacen(almacenDTO);
            almacen.setId(id);  // Aseguramos que usa el mismo ID
            Almacen almacenActualizado = almacenRepository.save(almacen);
            return convertirAAlmacenDTO(almacenActualizado);
        } else {
            throw new RuntimeException("Almacén no encontrado");
        }
    }

    // Eliminar un almacén
    public void eliminarAlmacen(Long id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
        } else {
            throw new RuntimeException("Almacén no encontrado");
        }
    }

    // Métodos para convertir entre entidad y DTO
    private AlmacenDTO convertirAAlmacenDTO(Almacen almacen) {
        return new AlmacenDTO(almacen.getId(), almacen.getNombre(), almacen.getDireccion(), almacen.getTipoAlmacen());
    }

    private Almacen convertirAAlmacen(AlmacenDTO almacenDTO) {
        Almacen almacen = new Almacen();
        almacen.setId(almacenDTO.getId());
        almacen.setNombre(almacenDTO.getNombre());
        almacen.setDireccion(almacenDTO.getDireccion());
        almacen.setTipoAlmacen(almacenDTO.getTipoAlmacen());
        return almacen;
    }
}
