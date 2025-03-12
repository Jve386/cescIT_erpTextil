package com.tiendatextil.service;

import com.tiendatextil.model.Almacen;
import com.tiendatextil.model.TipoAlmacen;
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
        validarTipoAlmacen(almacen.getTipoAlmacen());
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
        if (!almacenRepository.existsById(id)) {
            throw new RuntimeException("Almacén no encontrado");
        }
        validarTipoAlmacen(almacen.getTipoAlmacen());
        almacen.setId(id);
        return almacenRepository.save(almacen);
    }

    // Eliminar un almacén
    public void eliminarAlmacen(Long id) {
        if (!almacenRepository.existsById(id)) {
            throw new RuntimeException("Almacén no encontrado");
        }
        almacenRepository.deleteById(id);
    }

    // Validar el tipo de almacén
    private void validarTipoAlmacen(TipoAlmacen tipoAlmacen) {
        if (tipoAlmacen == null ||
                (tipoAlmacen != TipoAlmacen.TIENDA && tipoAlmacen != TipoAlmacen.ALMACEN)) {
            throw new IllegalArgumentException("Tipo de almacén inválido");
        }
    }
}