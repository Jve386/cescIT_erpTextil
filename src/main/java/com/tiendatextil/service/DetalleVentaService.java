// DetalleVentaService
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
        // Calcular los totales (sin IVA, IVA, precio total)
        calcularTotales(detalleVenta);
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
            // Recalcular los totales si la cantidad cambia
            calcularTotales(detalleVenta);
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

    // Metodo para calcular los totales (precio sin IVA, IVA, precio total)
    private void calcularTotales(DetalleVenta detalleVenta) {
        // Calcular precio sin IVA
        double precioSinIva = detalleVenta.getPrecioUnitario() * detalleVenta.getCantidad();

        // Calcular IVA (supuesto 21%)
        double iva = precioSinIva * 0.21;

        // Calcular precio total
        double precioTotal = precioSinIva + iva;

        // Asignar los valores calculados al detalle de venta
        detalleVenta.setPrecioSinIva(precioSinIva);
        detalleVenta.setIva(iva);
        detalleVenta.setPrecioTotal(precioTotal);
    }
}