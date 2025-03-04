package com.tiendatextil.service;

import com.tiendatextil.model.Almacen;
import com.tiendatextil.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    @Autowired
    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Crear un nuevo almacén
    public Almacen crearAlmacen(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    // Obtener todos los almacenes
    public List<Almacen> obtenerAlmacenes() {
        return almacenRepository.findAll();
    }

    // Obtener un almacén por su ID
    public Optional<Almacen> obtenerAlmacenPorId(Long id) {
        return almacenRepository.findById(id);
    }

    // Actualizar un almacén
    public Almacen actualizarAlmacen(Long id, Almacen almacen) {
        if (almacenRepository.existsById(id)) {
            almacen.setIdAlmacen(id); // Aseguramos que el ID no cambie
            return almacenRepository.save(almacen);
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
}
