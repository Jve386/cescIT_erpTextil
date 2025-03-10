package com.tiendatextil.controller;

import com.tiendatextil.dto.ArticuloDTO;
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
    public ResponseEntity<List<ArticuloDTO>> obtenerArticulos() {
        List<ArticuloDTO> articulos = articuloService.obtenerArticulos();
        return ResponseEntity.ok(articulos);
    }

    // Obtener un artículo por id
    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDTO> obtenerArticuloPorId(@PathVariable Long id) {
        Optional<ArticuloDTO> articuloDTO = articuloService.obtenerArticuloPorId(id);
        return articuloDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo artículo
    @PostMapping
    public ResponseEntity<ArticuloDTO> crearArticulo(@RequestBody ArticuloDTO articuloDTO) {
        ArticuloDTO nuevoArticuloDTO = articuloService.crearArticulo(articuloDTO);
        return new ResponseEntity<>(nuevoArticuloDTO, HttpStatus.CREATED);
    }

    // Actualizar un artículo
    @PutMapping("/{id}")
    public ResponseEntity<ArticuloDTO> actualizarArticulo(@PathVariable Long id, @RequestBody ArticuloDTO articuloDTO) {
        try {
            ArticuloDTO articuloActualizado = articuloService.actualizarArticulo(id, articuloDTO);
            return ResponseEntity.ok(articuloActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Enviar un 404 explícito si el artículo no es encontrado
        }
    }

    // Eliminar un artículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Long id) {
        try {
            articuloService.eliminarArticulo(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 si no se encuentra el artículo
        }
    }
}
