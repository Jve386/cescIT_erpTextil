package com.tiendatextil.service;

import com.tiendatextil.dto.ProductoDTO;
import com.tiendatextil.model.Categoria;
import com.tiendatextil.model.Producto;
import com.tiendatextil.repository.ProductoRepository;
import com.tiendatextil.repository.CategoriaRepository; // Asegúrate de tener el repositorio de Categoria
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;  // Añadir el repositorio de Categoria

    @Autowired
    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Crear un nuevo producto
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Categoria categoria = obtenerCategoriaPorId(productoDTO.getIdCategoria());
        Producto producto = convertirAEntidad(productoDTO, categoria);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    // Obtener todos los productos
    public List<ProductoDTO> obtenerProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener un producto por su ID
    public Optional<ProductoDTO> obtenerProductoPorId(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(this::convertirADTO);
    }

    // Actualizar un producto
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        if (productoRepository.existsById(id)) {
            Categoria categoria = obtenerCategoriaPorId(productoDTO.getIdCategoria());
            Producto productoExistente = convertirAEntidad(productoDTO, categoria);
            productoExistente.setId(id);
            Producto productoActualizado = productoRepository.save(productoExistente);
            return convertirADTO(productoActualizado);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    // Eliminar un producto
    public void eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    // Obtener categoría por ID
    private Categoria obtenerCategoriaPorId(Long idCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findById(idCategoria);
        if (categoria.isPresent()) {
            return categoria.get();
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioBase(),
                producto.getCategoria() != null ? producto.getCategoria().getId() : null
        );
    }

    private Producto convertirAEntidad(ProductoDTO productoDTO, Categoria categoria) {
        return new Producto(
                productoDTO.getNombre(),
                productoDTO.getDescripcion(),
                productoDTO.getPrecioBase(),
                categoria
        );
    }
}
