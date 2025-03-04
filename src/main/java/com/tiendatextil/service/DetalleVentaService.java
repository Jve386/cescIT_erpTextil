package com.tiendatextil.service;

import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    // Crear un nuevo detalle de venta
    public DetalleVenta crearDetalleVenta(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    // Obtener todos los detalles de venta
    public List<DetalleVenta> obtenerDetallesVenta() {
        return detalleVentaRepository.findAll();
    }

    // Obtener un detalle de venta por su ID
    public Optional<DetalleVenta> obtenerDetalleVentaPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    // Actualizar un detalle de venta
    public DetalleVenta actualizarDetalleVenta(Long id, DetalleVenta detalleVenta) {
        if (detalleVentaRepository.existsById(id)) {
            return detalleVentaRepository.save(detalleVenta);
        } else {
            throw new RuntimeException("Detalle de venta no encontrado");
        }
    }

    // Eliminar un detalle de venta
    public void eliminarDetalleVenta(Long id) {
        if (detalleVentaRepository.existsById(id)) {
            detalleVentaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Detalle de venta no encontrado");
        }
    }
}
