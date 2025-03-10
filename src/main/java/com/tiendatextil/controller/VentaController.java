package com.tiendatextil.controller;

import com.tiendatextil.dto.VentaDTO;
import com.tiendatextil.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<VentaDTO> crearVenta(@RequestBody VentaDTO ventaDTO) {
        try {
            // Retornamos la respuesta con el objeto VentaDTO creado y el código 201 Created
            VentaDTO createdVenta = ventaService.crearVenta(ventaDTO);
            return new ResponseEntity<>(createdVenta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // En caso de error, retornamos el código de estado 400 Bad Request
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> obtenerVentas() {
        List<VentaDTO> ventas = ventaService.obtenerVentas();
        return new ResponseEntity<>(ventas, HttpStatus.OK); // Respuesta con código 200 OK
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        Optional<VentaDTO> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // OK o Not Found
    }

    // Actualizar una venta
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizarVenta(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        try {
            VentaDTO updatedVenta = ventaService.actualizarVenta(id, ventaDTO);
            return new ResponseEntity<>(updatedVenta, HttpStatus.OK); // Respuesta con código 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Si hay error, código 400
        }
    }

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Respuesta con código 204 No Content
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra la venta, código 404
        }
    }
}
