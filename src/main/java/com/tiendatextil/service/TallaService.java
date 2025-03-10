package com.tiendatextil.service;

import com.tiendatextil.model.Talla;
import com.tiendatextil.repository.TallaRepository;
import com.tiendatextil.dto.TallaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TallaService {

    private final TallaRepository tallaRepository;

    @Autowired
    public TallaService(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    // Crear una nueva talla
    public TallaDTO crearTalla(TallaDTO tallaDTO) {
        Talla talla = new Talla(tallaDTO.getTalla());
        Talla nuevaTalla = tallaRepository.save(talla);
        return new TallaDTO(nuevaTalla.getId(), nuevaTalla.getTalla());
    }

    // Obtener todas las tallas
    public List<TallaDTO> obtenerTallas() {
        return tallaRepository.findAll().stream()
                .map(talla -> new TallaDTO(talla.getId(), talla.getTalla()))
                .collect(Collectors.toList());
    }

    // Obtener una talla por su ID
    public Optional<TallaDTO> obtenerTallaPorId(Long id) {
        return tallaRepository.findById(id).map(talla -> new TallaDTO(talla.getId(), talla.getTalla()));
    }

    // Actualizar una talla
    public TallaDTO actualizarTalla(Long id, TallaDTO tallaDTO) {
        if (tallaRepository.existsById(id)) {
            Talla talla = new Talla(tallaDTO.getTalla());
            talla.setId(id);
            Talla tallaActualizada = tallaRepository.save(talla);
            return new TallaDTO(tallaActualizada.getId(), tallaActualizada.getTalla());
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
