package com.tiendatextil.service;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepository;

    @Autowired
    public ArticuloService(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    // Crear un nuevo artículo
    public Articulo crearArticulo(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    // Obtener todos los artículos
    public List<Articulo> obtenerArticulos() {
        return articuloRepository.findAll();
    }

    // Obtener un artículo por su ID
    public Optional<Articulo> obtenerArticuloPorId(Long id) {
        return articuloRepository.findById(id);
    }

    // Actualizar un artículo
    public Articulo actualizarArticulo(Long id, Articulo articulo) {
        if (articuloRepository.existsById(id)) {
            articulo.setId(id); // Aseguramos que el ID no cambie
            return articuloRepository.save(articulo);
        } else {
            throw new RuntimeException("Artículo no encontrado");
        }
    }

    // Eliminar un artículo
    public void eliminarArticulo(Long id) {
        if (articuloRepository.existsById(id)) {
            articuloRepository.deleteById(id);
        } else {
            throw new RuntimeException("Artículo no encontrado");
        }
    }
}
