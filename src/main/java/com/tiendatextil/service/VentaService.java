package com.tiendatextil.service;

import com.tiendatextil.model.Venta;
import com.tiendatextil.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    // Crear una nueva venta
    public Venta crearVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por su ID
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // Actualizar una venta
    public Venta actualizarVenta(Long id, Venta venta) {
        if (ventaRepository.existsById(id)) {
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }

    // Eliminar una venta
    public void eliminarVenta(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
}
