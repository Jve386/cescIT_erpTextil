package com.tiendatextil.controller;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;

    @Autowired
    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    // Obtener todos los artículos
    @GetMapping
    public List<Articulo> obtenerArticulos() {
        return articuloService.obtenerArticulos();
    }

    // Obtener un artículo por id
    @GetMapping("/{id}")
    public ResponseEntity<Articulo> obtenerArticuloPorId(@PathVariable Long id) {
        Optional<Articulo> articulo = articuloService.obtenerArticuloPorId(id);
        return articulo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo artículo
    @PostMapping
    public ResponseEntity<Articulo> crearArticulo(@RequestBody Articulo articulo) {
        Articulo nuevoArticulo = articuloService.crearArticulo(articulo);
        return new ResponseEntity<>(nuevoArticulo, HttpStatus.CREATED);
    }

    // Actualizar un artículo
    @PutMapping("/{id}")
    public ResponseEntity<Articulo> actualizarArticulo(@PathVariable Long id, @RequestBody Articulo articulo) {
        try {
            Articulo articuloActualizado = articuloService.actualizarArticulo(id, articulo);
            return ResponseEntity.ok(articuloActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un artículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Long id) {
        try {
            articuloService.eliminarArticulo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
