package com.tiendatextil.controller;

import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<DetalleVenta> obtenerDetallesVenta() {
        return detalleVentaService.obtenerDetallesVenta();
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
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
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
}
