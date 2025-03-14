package com.tiendatextil.controller;

import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.service.DetalleVentaService;
import com.tiendatextil.dto.DetalleVentaDTO;
import com.tiendatextil.dto.ArticuloDTO;
import com.tiendatextil.model.Articulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    @Autowired
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Obtener todos los detalles de venta
    @GetMapping
    @Transactional(readOnly = true)
    public List<DetalleVentaDTO> obtenerDetallesVenta() {
        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerDetallesVenta();
        return detallesVenta.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private DetalleVentaDTO mapToDTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();

        // Check if articulo is null before mapping it
        if (detalle.getArticulo() != null) {
            ArticuloDTO articuloDTO = mapToArticuloDTO(detalle.getArticulo());

            dto.setIdArticulo(articuloDTO.getId());
            dto.setNombreProducto(articuloDTO.getNombreProducto());
            dto.setTalla(articuloDTO.getTalla());
            dto.setColor(articuloDTO.getColor());
        }

        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setPrecioTotal(detalle.getPrecioTotal());

        return dto;
    }

    // Obtener un detalle de venta por id
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVentaPorId(@PathVariable Long id) {
        Optional<DetalleVenta> detalleVenta = detalleVentaService.obtenerDetalleVentaPorId(id);
        return detalleVenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<DetalleVenta> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        DetalleVenta nuevoDetalleVenta = detalleVentaService.crearDetalleVenta(detalleVenta);
        return new ResponseEntity<>(nuevoDetalleVenta, HttpStatus.CREATED);
    }

    // Actualizar un detalle de venta
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(@PathVariable Long id,
            @RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta detalleVentaActualizado = detalleVentaService.actualizarDetalleVenta(id, detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un detalle de venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        try {
            detalleVentaService.eliminarDetalleVenta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener detalles de venta por id de venta
    @GetMapping("/venta/{id}")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorVenta(@PathVariable Long id) {
        List<DetalleVenta> detalles = detalleVentaService.obtenerDetallesPorVenta(id);
        return ResponseEntity.ok(detalles);
    }

    // Obtener detalles de venta por id de artículo
    @GetMapping("/articulo/{id}")
    public ResponseEntity<List<DetalleVentaDTO>> obtenerDetallesPorArticulo(@PathVariable Long id) {
        List<DetalleVenta> detalles = detalleVentaService.obtenerDetallesPorArticulo(id);
        List<DetalleVentaDTO> detallesDTO = detalles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(detallesDTO);
    }

    private ArticuloDTO mapToArticuloDTO(Articulo articulo) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId());

        // Add null checks for nested objects
        if (articulo.getProducto() != null) {
            dto.setNombreProducto(articulo.getProducto().getNombre());
        }

        if (articulo.getTalla() != null) {
            dto.setTalla(articulo.getTalla().getTalla());
        }

        if (articulo.getColor() != null) {
            dto.setColor(articulo.getColor().getColor());
        }

        dto.setPrecioCoste(articulo.getPrecio());
        dto.setPrecioVenta(articulo.getPrecioVenta());
        return dto;
    }
}
