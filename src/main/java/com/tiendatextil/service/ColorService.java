package com.tiendatextil.service;

import com.tiendatextil.model.Color;
import com.tiendatextil.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    // Crear un nuevo color
    public Color crearColor(Color color) {
        return colorRepository.save(color);
    }

    // Obtener todos los colores
    public List<Color> obtenerColores() {
        return colorRepository.findAll();
    }

    // Obtener un color por su ID
    public Optional<Color> obtenerColorPorId(Long id) {
        return colorRepository.findById(id);
    }

    // Actualizar un color
    public Color actualizarColor(Long id, Color color) {
        if (colorRepository.existsById(id)) {
            color.setId(id);
            return colorRepository.save(color);
        } else {
            throw new RuntimeException("Color no encontrado");
        }
    }

    // Eliminar un color
    public void eliminarColor(Long id) {
        if (colorRepository.existsById(id)) {
            colorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Color no encontrado");
        }
    }
}
