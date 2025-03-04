package com.tiendatextil.service;

import com.tiendatextil.model.Talla;
import com.tiendatextil.repository.TallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TallaService {

    private final TallaRepository tallaRepository;

    @Autowired
    public TallaService(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    // Crear una nueva talla
    public Talla crearTalla(Talla talla) {
        return tallaRepository.save(talla);
    }

    // Obtener todas las tallas
    public List<Talla> obtenerTallas() {
        return tallaRepository.findAll();
    }

    // Obtener una talla por su ID
    public Optional<Talla> obtenerTallaPorId(Long id) {
        return tallaRepository.findById(id);
    }

    // Actualizar una talla
    public Talla actualizarTalla(Long id, Talla talla) {
        if (tallaRepository.existsById(id)) {
            talla.setIdService(id);  // Aseguramos que el ID no cambie
            return tallaRepository.save(talla);
        } else {
            throw new RuntimeException("Talla no encontrada");
        }
    }

    // Eliminar una talla
    public void eliminarTalla(Long id) {
        if (tallaRepository.existsById(id)) {
            tallaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Talla no encontrada");
        }
    }
}
