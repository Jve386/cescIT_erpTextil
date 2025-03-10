package com.tiendatextil.service;

import com.tiendatextil.dto.DetalleVentaDTO;
import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    // Crear un nuevo detalle de venta
    public DetalleVentaDTO crearDetalleVenta(DetalleVentaDTO detalleVentaDTO) {
        DetalleVenta detalleVenta = convertirADetalleVenta(detalleVentaDTO);
        DetalleVenta detalleVentaGuardado = detalleVentaRepository.save(detalleVenta);
        return convertirADetalleVentaDTO(detalleVentaGuardado);
    }

    // Obtener todos los detalles de venta
    public List<DetalleVentaDTO> obtenerDetallesVenta() {
        List<DetalleVenta> detallesVenta = detalleVentaRepository.findAll();
        return detallesVenta.stream()
                .map(this::convertirADetalleVentaDTO)
                .collect(Collectors.toList());
    }

    // Obtener un detalle de venta por su ID
    public Optional<DetalleVentaDTO> obtenerDetalleVentaPorId(Long id) {
        return detalleVentaRepository.findById(id).map(this::convertirADetalleVentaDTO);
    }

    // Actualizar un detalle de venta
    public DetalleVentaDTO actualizarDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO) {
        return detalleVentaRepository.findById(id)
                .map(detalleExistente -> {
                    detalleExistente.setCantidad(detalleVentaDTO.getCantidad());
                    detalleExistente.setPrecioUnitario(detalleVentaDTO.getPrecioUnitario());
                    detalleExistente.setPrecioSinIva(detalleVentaDTO.getPrecioSinIva());
                    detalleExistente.setIva(detalleVentaDTO.getIva());
                    detalleExistente.setPrecioTotal(detalleVentaDTO.getPrecioTotal());
                    DetalleVenta detalleVentaActualizado = detalleVentaRepository.save(detalleExistente);
                    return convertirADetalleVentaDTO(detalleVentaActualizado);
                })
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));
    }

    // Eliminar un detalle de venta
    public void eliminarDetalleVenta(Long id) {
        if (detalleVentaRepository.existsById(id)) {
            detalleVentaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Detalle de venta no encontrado");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private DetalleVentaDTO convertirADetalleVentaDTO(DetalleVenta detalleVenta) {
        return new DetalleVentaDTO(
                detalleVenta.getId(),
                detalleVenta.getVenta().getId(),
                detalleVenta.getArticulo().getId(),
                detalleVenta.getCantidad(),
                detalleVenta.getPrecioUnitario(),
                detalleVenta.getPrecioSinIva(),
                detalleVenta.getIva(),
                detalleVenta.getPrecioTotal()
        );
    }

    private DetalleVenta convertirADetalleVenta(DetalleVentaDTO detalleVentaDTO) {
        // Asumiendo que ya existen métodos para obtener Venta y Articulo por sus ids
        // Esto podría requerir que inyectes las clases de Venta y Articulo en este servicio o que los pasemos directamente.
        return new DetalleVenta(
                null,  // Venta y Articulo se deben obtener de su respectivo servicio o repositorio
                null,  // Este paso puede requerir más lógica dependiendo de tu implementación
                detalleVentaDTO.getCantidad(),
                detalleVentaDTO.getPrecioUnitario(),
                detalleVentaDTO.getPrecioSinIva(),
                detalleVentaDTO.getIva(),
                detalleVentaDTO.getPrecioTotal()
        );
    }
}
