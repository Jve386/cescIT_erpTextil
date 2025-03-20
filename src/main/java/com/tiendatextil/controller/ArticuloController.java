package com.tiendatextil.controller;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.service.ArticuloService;
import com.tiendatextil.dto.ArticuloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private static final Logger logger = LoggerFactory.getLogger(ArticuloController.class);

    @Autowired
    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
        logger.info("ArticuloController initialized");
    }

    // Obtener todos los artículos
    @GetMapping
    public ResponseEntity<List<ArticuloDTO>> obtenerArticulos() {
        logger.info("Received request to get all articles");
        try {
            logger.info("Calling articuloService.obtenerArticulos()");
            List<Articulo> articulos = articuloService.obtenerArticulos();
            logger.info("Retrieved {} articles from service", articulos.size());
            
            logger.info("Starting to map articles to DTOs");
            List<ArticuloDTO> articulosDTO = articulos.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            logger.info("Successfully mapped {} articles to DTOs", articulosDTO.size());
            return ResponseEntity.ok(articulosDTO);
        } catch (Exception e) {
            logger.error("Error occurred while getting articles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Obtener un artículo por id
    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDTO> obtenerArticuloPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo artículo con ID: {}", id);
            Optional<Articulo> articulo = articuloService.obtenerArticuloPorId(id);
            return articulo.map(a -> ResponseEntity.ok(mapToDTO(a)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al obtener artículo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Crear un nuevo artículo
    @PostMapping
    public ResponseEntity<ArticuloDTO> crearArticulo(@RequestBody Articulo articulo) {
        try {
            logger.info("Creando nuevo artículo");
            Articulo nuevoArticulo = articuloService.crearArticulo(articulo);
            return new ResponseEntity<>(mapToDTO(nuevoArticulo), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear artículo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Actualizar un artículo
    @PutMapping("/{id}")
    public ResponseEntity<ArticuloDTO> actualizarArticulo(@PathVariable Long id, @RequestBody Articulo articulo) {
        try {
            logger.info("Actualizando artículo con ID: {}", id);
            logger.debug("Datos recibidos para actualización: {}", articulo);
            
            // Verificar si el artículo existe
            Optional<Articulo> articuloExistente = articuloService.obtenerArticuloPorId(id);
            if (articuloExistente.isEmpty()) {
                logger.error("No se encontró el artículo con ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            // Actualizar los campos de precio
            Articulo articuloActual = articuloExistente.get();
            articuloActual.setPrecioCoste(articulo.getPrecioCoste());
            articuloActual.setPrecioVenta(articulo.getPrecioVenta());
            
            logger.debug("Actualizando precios - Coste: {}, Venta: {}", 
                articuloActual.getPrecioCoste(), articuloActual.getPrecioVenta());
            
            Articulo articuloActualizado = articuloService.actualizarArticulo(id, articuloActual);
            logger.info("Artículo actualizado exitosamente: {}", articuloActualizado);
            
            return ResponseEntity.ok(mapToDTO(articuloActualizado));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar artículo con ID: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar artículo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar un artículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Long id) {
        try {
            logger.info("Eliminando artículo con ID: {}", id);
            articuloService.eliminarArticulo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar artículo con ID: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar artículo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ArticuloDTO mapToDTO(Articulo articulo) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId());
        
        if (articulo.getProducto() != null) {
            dto.setNombreProducto(articulo.getProducto().getNombre());
            logger.debug("Setting product name: {}", articulo.getProducto().getNombre());
        } else {
            logger.warn("Product is null for article ID: {}", articulo.getId());
        }
        
        if (articulo.getTalla() != null) {
            dto.setTalla(articulo.getTalla().getTalla());
            logger.debug("Setting size: {}", articulo.getTalla().getTalla());
        } else {
            logger.warn("Size is null for article ID: {}", articulo.getId());
        }
        
        if (articulo.getColor() != null) {
            dto.setColor(articulo.getColor().getColor());
            logger.debug("Setting color: {}", articulo.getColor().getColor());
        } else {
            logger.warn("Color is null for article ID: {}", articulo.getId());
        }
        
        dto.setPrecioCoste(articulo.getPrecioCoste());
        logger.debug("Setting cost price: {}", articulo.getPrecioCoste());
        
        dto.setPrecioVenta(articulo.getPrecioVenta());
        logger.debug("Setting sale price: {}", articulo.getPrecioVenta());
        
        return dto;
    }
}
