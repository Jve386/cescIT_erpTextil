package com.tiendatextil.controller;

import com.tiendatextil.dto.ColorDTO;
import com.tiendatextil.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/colores")
public class ColorController {

    private final ColorService colorService;

    @Autowired
    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    // Obtener todos los colores
    @GetMapping
    public List<ColorDTO> obtenerColores() {
        return colorService.obtenerColores();
    }

    // Obtener un color por id
    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> obtenerColorPorId(@PathVariable Long id) {
        Optional<ColorDTO> colorDTO = colorService.obtenerColorPorId(id);
        return colorDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo color
    @PostMapping
    public ResponseEntity<ColorDTO> crearColor(@RequestBody ColorDTO colorDTO) {
        ColorDTO nuevoColorDTO = colorService.crearColor(colorDTO);
        return new ResponseEntity<>(nuevoColorDTO, HttpStatus.CREATED);
    }

    // Actualizar un color
    @PutMapping("/{id}")
    public ResponseEntity<ColorDTO> actualizarColor(@PathVariable Long id, @RequestBody ColorDTO colorDTO) {
        try {
            ColorDTO colorActualizado = colorService.actualizarColor(id, colorDTO);
            return ResponseEntity.ok(colorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un color
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColor(@PathVariable Long id) {
        try {
            colorService.eliminarColor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
