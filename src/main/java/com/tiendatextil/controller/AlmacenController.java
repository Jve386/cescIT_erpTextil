package com.tiendatextil.controller;

import com.tiendatextil.model.Almacen;
import com.tiendatextil.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    @Autowired
    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    // Obtener todos los almacenes
    @GetMapping
    public List<Almacen> obtenerAlmacenes() {
        return almacenService.obtenerAlmacenes();
    }

    // Obtener un almacén por id
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerAlmacenPorId(@PathVariable Long id) {
        Optional<Almacen> almacen = almacenService.obtenerAlmacenPorId(id);
        return almacen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo almacén
    @PostMapping
    public ResponseEntity<Almacen> crearAlmacen(@RequestBody Almacen almacen) {
        try {
            Almacen nuevoAlmacen = almacenService.crearAlmacen(almacen);
            return new ResponseEntity<>(nuevoAlmacen, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un almacén
    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable Long id, @RequestBody Almacen almacen) {
        try {
            Almacen almacenActualizado = almacenService.actualizarAlmacen(id, almacen);
            return ResponseEntity.ok(almacenActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un almacén
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        try {
            almacenService.eliminarAlmacen(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
