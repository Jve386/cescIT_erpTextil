package com.tiendatextil.service;

import com.tiendatextil.model.TipoAlmacen;
import com.tiendatextil.repository.TipoAlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoAlmacenService {

    private final TipoAlmacenRepository tipoAlmacenRepository;

    @Autowired
    public TipoAlmacenService(TipoAlmacenRepository tipoAlmacenRepository) {
        this.tipoAlmacenRepository = tipoAlmacenRepository;
    }

    // Crear un nuevo tipo de almacén
    public TipoAlmacen crearTipoAlmacen(TipoAlmacen tipoAlmacen) {
        return tipoAlmacenRepository.save(tipoAlmacen);
    }

    // Obtener todos los tipos de almacén
    public List<TipoAlmacen> obtenerTiposAlmacen() {
        return tipoAlmacenRepository.findAll();
    }

    // Obtener un tipo de almacén por su ID
    public Optional<TipoAlmacen> obtenerTipoAlmacenPorId(Long id) {
        return tipoAlmacenRepository.findById(id);
    }

    // Actualizar un tipo de almacén
    public TipoAlmacen actualizarTipoAlmacen(Long id, TipoAlmacen tipoAlmacen) {
        if (tipoAlmacenRepository.existsById(id)) {
            tipoAlmacen.setId(id);
            return tipoAlmacenRepository.save(tipoAlmacen);
        } else {
            throw new RuntimeException("Tipo de almacén no encontrado");
        }
    }

    // Eliminar un tipo de almacén
    public void eliminarTipoAlmacen(Long id) {
        if (tipoAlmacenRepository.existsById(id)) {
            tipoAlmacenRepository.deleteById(id);
        } else {
            throw new RuntimeException("Tipo de almacén no encontrado");
        }
    }
}
