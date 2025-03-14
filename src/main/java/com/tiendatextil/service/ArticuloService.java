package com.tiendatextil.service;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepository;
    private static final Logger logger = LoggerFactory.getLogger(ArticuloService.class);

    @Autowired
    public ArticuloService(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    public List<Articulo> obtenerArticulos() {
        try {
            logger.info("Obteniendo todos los artículos desde el servicio");
            List<Articulo> articulos = articuloRepository.findAll();
            logger.info("Se encontraron {} artículos en la base de datos", articulos.size());
            return articulos;
        } catch (Exception e) {
            logger.error("Error al obtener artículos desde el servicio", e);
            throw e;
        }
    }

    public Optional<Articulo> obtenerArticuloPorId(Long id) {
        try {
            logger.info("Obteniendo artículo con ID: {} desde el servicio", id);
            return articuloRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error al obtener artículo con ID: {} desde el servicio", id, e);
            throw e;
        }
    }

    public Articulo crearArticulo(Articulo articulo) {
        try {
            logger.info("Creando nuevo artículo desde el servicio");
            return articuloRepository.save(articulo);
        } catch (Exception e) {
            logger.error("Error al crear artículo desde el servicio", e);
            throw e;
        }
    }

    public Articulo actualizarArticulo(Long id, Articulo articulo) {
        try {
            logger.info("Iniciando actualización de artículo con ID: {}", id);
            
            // Verificar si el artículo existe
            Optional<Articulo> articuloExistente = articuloRepository.findById(id);
            if (articuloExistente.isEmpty()) {
                logger.error("No se encontró el artículo con ID: {}", id);
                throw new RuntimeException("No se encontró el artículo con ID: " + id);
            }

            // Obtener el artículo existente
            Articulo articuloActual = articuloExistente.get();
            
            // Actualizar solo el campo de precio
            articuloActual.setPrecio(articulo.getPrecio());
            
            logger.debug("Actualizando precio del artículo {} - Coste: {}", 
                id, articuloActual.getPrecio());
            
            // Guardar los cambios
            Articulo articuloActualizado = articuloRepository.save(articuloActual);
            logger.info("Artículo actualizado exitosamente: {}", articuloActualizado);
            
            return articuloActualizado;
        } catch (Exception e) {
            logger.error("Error al actualizar artículo con ID: {}", id, e);
            throw e;
        }
    }

    public void eliminarArticulo(Long id) {
        try {
            logger.info("Eliminando artículo con ID: {} desde el servicio", id);
            if (!articuloRepository.existsById(id)) {
                throw new RuntimeException("Artículo no encontrado");
            }
            articuloRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar artículo con ID: {} desde el servicio", id, e);
            throw e;
        }
    }

    public Optional<Articulo> findByProductoNombreAndTallaTallaAndColorColor(String nombreProducto, String talla, String color) {
        try {
            logger.info("Buscando artículo por nombre: {}, talla: {}, color: {}", nombreProducto, talla, color);
            return articuloRepository.findByProductoNombreAndTallaTallaAndColorColor(nombreProducto, talla, color);
        } catch (Exception e) {
            logger.error("Error al buscar artículo por nombre: {}, talla: {}, color: {}", nombreProducto, talla, color, e);
            throw e;
        }
    }
}

