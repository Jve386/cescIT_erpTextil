package com.tiendatextil.controller;

import com.tiendatextil.model.Venta;
import com.tiendatextil.dto.VentaDTO; 
import com.tiendatextil.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ObjectMapper objectMapper; 

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
        this.objectMapper = new ObjectMapper();
    }

    // Obtener todas las ventas
    @GetMapping
    public List<VentaDTO> obtenerVentas() {
        List<VentaDTO> ventas = ventaService.obtenerVentasDTO();
                // Serialize and log ventas
                try {
                    String json = objectMapper.writeValueAsString(ventas);
                    System.out.println("Serialized Ventas: " + json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
        System.out.println("Ventas: " + ventas); // Log the ventas
        return ventas;
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        Optional<VentaDTO> venta = ventaService.obtenerVentaDTOPorId(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Venta venta) {
        try {
            venta.setNumeroTicket(null); // Permitir que el backend genere el número de ticket
            Venta nuevaVenta = ventaService.crearVenta(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Artículo no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else if (e.getMessage().contains("No hay suficiente stock")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    // Actualizar una venta
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}
