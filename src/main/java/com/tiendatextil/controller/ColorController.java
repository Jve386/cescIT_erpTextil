package com.tiendatextil.controller;

import com.tiendatextil.model.Color;
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
    public List<Color> obtenerColores() {
        return colorService.obtenerColores();
    }

    // Obtener un color por id
    @GetMapping("/{id}")
    public ResponseEntity<Color> obtenerColorPorId(@PathVariable Long id) {
        Optional<Color> color = colorService.obtenerColorPorId(id);
        return color.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo color
    @PostMapping
    public ResponseEntity<Color> crearColor(@RequestBody Color color) {
        Color nuevoColor = colorService.crearColor(color);
        return new ResponseEntity<>(nuevoColor, HttpStatus.CREATED);
    }

    // Actualizar un color
    @PutMapping("/{id}")
    public ResponseEntity<Color> actualizarColor(@PathVariable Long id, @RequestBody Color color) {
        try {
            Color colorActualizado = colorService.actualizarColor(id, color);
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
