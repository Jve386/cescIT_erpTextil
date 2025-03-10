package com.tiendatextil.service;

import com.tiendatextil.dto.ArticuloDTO;
import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.Producto;
import com.tiendatextil.model.Talla;
import com.tiendatextil.model.Color;
import com.tiendatextil.repository.ArticuloRepository;
import com.tiendatextil.repository.ProductoRepository; 
import com.tiendatextil.repository.TallaRepository;   
import com.tiendatextil.repository.ColorRepository;   
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final ProductoRepository productoRepository;
    private final TallaRepository tallaRepository;
    private final ColorRepository colorRepository;

    @Autowired
    public ArticuloService(ArticuloRepository articuloRepository, ProductoRepository productoRepository, 
                           TallaRepository tallaRepository, ColorRepository colorRepository) {
        this.articuloRepository = articuloRepository;
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
        this.colorRepository = colorRepository;
    }

    // Crear un nuevo artículo
    public ArticuloDTO crearArticulo(ArticuloDTO articuloDTO) {
        Articulo articulo = convertirAArticulo(articuloDTO);
        Articulo articuloGuardado = articuloRepository.save(articulo);
        return convertirAArticuloDTO(articuloGuardado);
    }

    // Obtener todos los artículos
    public List<ArticuloDTO> obtenerArticulos() {
        List<Articulo> articulos = articuloRepository.findAll();
        return articulos.stream()
                        .map(this::convertirAArticuloDTO)
                        .collect(Collectors.toList());
    }

    // Obtener un artículo por su ID
    public Optional<ArticuloDTO> obtenerArticuloPorId(Long id) {
        return articuloRepository.findById(id).map(this::convertirAArticuloDTO);
    }

    // Actualizar un artículo
    public ArticuloDTO actualizarArticulo(Long id, ArticuloDTO articuloDTO) {
        return articuloRepository.findById(id)
                .map(articuloExistente -> {
                    // Buscar las entidades Producto, Talla y Color por ID
                    Producto producto = productoRepository.findById(articuloDTO.getIdProducto())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                    Talla talla = tallaRepository.findById(articuloDTO.getIdTalla())
                            .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
                    Color color = colorRepository.findById(articuloDTO.getIdColor())
                            .orElseThrow(() -> new RuntimeException("Color no encontrado"));

                    // Asignar los valores al artículo existente
                    articuloExistente.setProducto(producto);
                    articuloExistente.setTalla(talla);
                    articuloExistente.setColor(color);
                    articuloExistente.setPrecio(articuloDTO.getPrecio());

                    Articulo articuloActualizado = articuloRepository.save(articuloExistente);
                    return convertirAArticuloDTO(articuloActualizado);
                })
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
    }

    // Eliminar un artículo
    public void eliminarArticulo(Long id) {
        if (articuloRepository.existsById(id)) {
            articuloRepository.deleteById(id);
        } else {
            throw new RuntimeException("Artículo no encontrado");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private ArticuloDTO convertirAArticuloDTO(Articulo articulo) {
        return new ArticuloDTO(
            articulo.getId(),
            articulo.getProducto().getId(),
            articulo.getTalla().getId(),
            articulo.getColor().getId(),
            articulo.getPrecio()
        );
    }

    private Articulo convertirAArticulo(ArticuloDTO articuloDTO) {
        // Buscar las entidades Producto, Talla y Color por ID
        Producto producto = productoRepository.findById(articuloDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Talla talla = tallaRepository.findById(articuloDTO.getIdTalla())
                .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
        Color color = colorRepository.findById(articuloDTO.getIdColor())
                .orElseThrow(() -> new RuntimeException("Color no encontrado"));

        Articulo articulo = new Articulo();
        articulo.setId(articuloDTO.getId());
        articulo.setProducto(producto);
        articulo.setTalla(talla);
        articulo.setColor(color);
        articulo.setPrecio(articuloDTO.getPrecio());
        return articulo;
    }
}