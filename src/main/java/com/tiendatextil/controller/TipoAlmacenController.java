package com.tiendatextil.controller;

import com.tiendatextil.model.TipoAlmacen;
import com.tiendatextil.service.TipoAlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipoalmacen")
public class TipoAlmacenController {

    private final TipoAlmacenService tipoAlmacenService;

    @Autowired
    public TipoAlmacenController(TipoAlmacenService tipoAlmacenService) {
        this.tipoAlmacenService = tipoAlmacenService;
    }

    // Obtener todos los tipos de almacén
    @GetMapping
    public List<TipoAlmacen> obtenerTiposAlmacen() {
        return tipoAlmacenService.obtenerTiposAlmacen();
    }

    // Obtener un tipo de almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoAlmacen> obtenerTipoAlmacenPorId(@PathVariable Long id) {
        Optional<TipoAlmacen> tipoAlmacen = tipoAlmacenService.obtenerTipoAlmacenPorId(id);
        return tipoAlmacen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo tipo de almacén
    @PostMapping
    public ResponseEntity<TipoAlmacen> crearTipoAlmacen(@RequestBody TipoAlmacen tipoAlmacen) {
        TipoAlmacen nuevoTipoAlmacen = tipoAlmacenService.crearTipoAlmacen(tipoAlmacen);
        return new ResponseEntity<>(nuevoTipoAlmacen, HttpStatus.CREATED);
    }

    // Actualizar un tipo de almacén
    @PutMapping("/{id}")
    public ResponseEntity<TipoAlmacen> actualizarTipoAlmacen(@PathVariable Long id, @RequestBody TipoAlmacen tipoAlmacen) {
        try {
            TipoAlmacen tipoAlmacenActualizado = tipoAlmacenService.actualizarTipoAlmacen(id, tipoAlmacen);
            return ResponseEntity.ok(tipoAlmacenActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un tipo de almacén
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoAlmacen(@PathVariable Long id) {
        try {
            tipoAlmacenService.eliminarTipoAlmacen(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
