package com.tiendatextil.service;

import com.tiendatextil.dto.ColorDTO;
import com.tiendatextil.model.Color;
import com.tiendatextil.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    // Crear un nuevo color
    public ColorDTO crearColor(ColorDTO colorDTO) {
        Color color = convertirAColor(colorDTO);
        Color colorGuardado = colorRepository.save(color);
        return convertirAColorDTO(colorGuardado);
    }

    // Obtener todos los colores
    public List<ColorDTO> obtenerColores() {
        List<Color> colores = colorRepository.findAll();
        return colores.stream()
                      .map(this::convertirAColorDTO)
                      .collect(Collectors.toList());
    }

    // Obtener un color por su ID
    public Optional<ColorDTO> obtenerColorPorId(Long id) {
        return colorRepository.findById(id).map(this::convertirAColorDTO);
    }

    // Actualizar un color
    public ColorDTO actualizarColor(Long id, ColorDTO colorDTO) {
        return colorRepository.findById(id)
                .map(colorExistente -> {
                    colorExistente.setColor(colorDTO.getColor());
                    Color colorActualizado = colorRepository.save(colorExistente);
                    return convertirAColorDTO(colorActualizado);
                })
                .orElseThrow(() -> new RuntimeException("Color no encontrado"));
    }

    // Eliminar un color
    public void eliminarColor(Long id) {
        if (colorRepository.existsById(id)) {
            colorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Color no encontrado");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private ColorDTO convertirAColorDTO(Color color) {
        return new ColorDTO(color.getId(), color.getColor());
    }

    private Color convertirAColor(ColorDTO colorDTO) {
        return new Color(colorDTO.getColor());
    }
}
