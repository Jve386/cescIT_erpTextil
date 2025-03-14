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
            Articulo articuloActualizado = articuloService.actualizarArticulo(id, articulo);
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
        try {
            logger.debug("Mapping article to DTO: {}", articulo.getId());
            ArticuloDTO dto = new ArticuloDTO();
            dto.setId(articulo.getId());
            
            if (articulo.getProducto() != null) {
                dto.setNombreProducto(articulo.getProducto().getNombre());
                logger.debug("Set product name: {}", articulo.getProducto().getNombre());
            } else {
                logger.warn("Article {} has no associated product", articulo.getId());
                dto.setNombreProducto("Sin nombre");
            }
            
            if (articulo.getTalla() != null) {
                dto.setTalla(articulo.getTalla().getTalla());
                logger.debug("Set size: {}", articulo.getTalla().getTalla());
            } else {
                logger.warn("Article {} has no associated size", articulo.getId());
                dto.setTalla("Sin talla");
            }
            
            if (articulo.getColor() != null) {
                dto.setColor(articulo.getColor().getColor());
                logger.debug("Set color: {}", articulo.getColor().getColor());
            } else {
                logger.warn("Article {} has no associated color", articulo.getId());
                dto.setColor("Sin color");
            }
            
            // Handle null prices
            Double precio = articulo.getPrecio();
            Double precioVenta = articulo.getPrecioVenta();
            
            dto.setPrecioCoste(precio);
            dto.setPrecioVenta(precioVenta);
            
            logger.debug("Set prices - cost: {}, sale: {}", precio, precioVenta);
            
            logger.debug("Successfully mapped article {} to DTO", articulo.getId());
            return dto;
        } catch (Exception e) {
            logger.error("Error mapping article {} to DTO", articulo.getId(), e);
            throw e;
        }
    }
}
