package com.tiendatextil.controller;

import com.tiendatextil.dto.TallaDTO;
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
    public List<TallaDTO> obtenerTallas() {
        return tallaService.obtenerTallas();
    }

    // Obtener una talla por id
    @GetMapping("/{id}")
    public ResponseEntity<TallaDTO> obtenerTallaPorId(@PathVariable Long id) {
        Optional<TallaDTO> talla = tallaService.obtenerTallaPorId(id);
        return talla.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva talla
    @PostMapping
    public ResponseEntity<TallaDTO> crearTalla(@RequestBody TallaDTO tallaDTO) {
        try {
            TallaDTO nuevaTalla = tallaService.crearTalla(tallaDTO);
            return new ResponseEntity<>(nuevaTalla, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // En caso de error
        }
    }

    // Actualizar una talla
    @PutMapping("/{id}")
    public ResponseEntity<TallaDTO> actualizarTalla(@PathVariable Long id, @RequestBody TallaDTO tallaDTO) {
        try {
            TallaDTO tallaActualizada = tallaService.actualizarTalla(id, tallaDTO);
            return ResponseEntity.ok(tallaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Si no se encuentra la talla
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
