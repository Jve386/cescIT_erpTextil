package com.tiendatextil.service;

import com.tiendatextil.dto.CategoriaDTO;
import com.tiendatextil.model.Categoria;
import com.tiendatextil.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Crear una nueva categoría
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertirACategoria(categoriaDTO);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirACategoriaDTO(categoriaGuardada);
    }

    // Obtener todas las categorías
    public List<CategoriaDTO> obtenerCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                         .map(this::convertirACategoriaDTO)
                         .collect(Collectors.toList());
    }

    // Obtener una categoría por su ID
    public Optional<CategoriaDTO> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).map(this::convertirACategoriaDTO);
    }

    // Actualizar una categoría
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        return categoriaRepository.findById(id)
                .map(categoriaExistente -> {
                    categoriaExistente.setNombre(categoriaDTO.getNombre());
                    Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
                    return convertirACategoriaDTO(categoriaActualizada);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // Eliminar una categoría
    public void eliminarCategoria(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private CategoriaDTO convertirACategoriaDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }

    private Categoria convertirACategoria(CategoriaDTO categoriaDTO) {
        return new Categoria(categoriaDTO.getNombre());
    }
}
