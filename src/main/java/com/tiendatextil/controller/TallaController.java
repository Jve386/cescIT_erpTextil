package com.tiendatextil.controller;

import com.tiendatextil.model.Talla;
import com.tiendatextil.service.TallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tallas")
public class TallaController {

    private final TallaService tallaService;

    @Autowired
    public TallaController(TallaService tallaService) {
        this.tallaService = tallaService;
    }

    // Obtener todas las tallas
    @GetMapping
    public List<Talla> obtenerTallas() {
        return tallaService.obtenerTallas();
    }

    // Obtener una talla por id
    @GetMapping("/{id}")
    public ResponseEntity<Talla> obtenerTallaPorId(@PathVariable Long id) {
        Optional<Talla> talla = tallaService.obtenerTallaPorId(id);
        return talla.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva talla
    @PostMapping
    public ResponseEntity<Talla> crearTalla(@RequestBody Talla talla) {
        Talla nuevaTalla = tallaService.crearTalla(talla);
        return new ResponseEntity<>(nuevaTalla, HttpStatus.CREATED);
    }

    // Actualizar una talla
    @PutMapping("/{id}")
    public ResponseEntity<Talla> actualizarTalla(@PathVariable Long id, @RequestBody Talla talla) {
        try {
            Talla tallaActualizada = tallaService.actualizarTalla(id, talla);
            return ResponseEntity.ok(tallaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una talla
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTalla(@PathVariable Long id) {
        try {
            tallaService.eliminarTalla(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
