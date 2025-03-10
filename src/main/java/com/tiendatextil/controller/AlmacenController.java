package com.tiendatextil.controller;

import com.tiendatextil.dto.AlmacenDTO;
import com.tiendatextil.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    @Autowired
    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    // Obtener todos los almacenes
    @GetMapping
    public List<AlmacenDTO> obtenerAlmacenes() {
        return almacenService.obtenerAlmacenes();
    }

    // Obtener un almacén por id
    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerAlmacenPorId(@PathVariable Long id) {
        Optional<AlmacenDTO> almacenDTO = almacenService.obtenerAlmacenPorId(id);
        return almacenDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo almacén
    @PostMapping
    public ResponseEntity<AlmacenDTO> crearAlmacen(@RequestBody AlmacenDTO almacenDTO) {
        try {
            if (almacenDTO.getTipoAlmacen() == null || 
                (!almacenDTO.getTipoAlmacen().equals("Tienda") && !almacenDTO.getTipoAlmacen().equals("Almacén"))) {
                return ResponseEntity.badRequest().build();
            }
            AlmacenDTO nuevoAlmacenDTO = almacenService.crearAlmacen(almacenDTO);
            return new ResponseEntity<>(nuevoAlmacenDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un almacén
    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDTO> actualizarAlmacen(@PathVariable Long id, @RequestBody AlmacenDTO almacenDTO) {
        try {
            if (almacenDTO.getTipoAlmacen() == null || 
                (!almacenDTO.getTipoAlmacen().equals("Tienda") && !almacenDTO.getTipoAlmacen().equals("Almacén"))) {
                return ResponseEntity.badRequest().build();
            }
            AlmacenDTO almacenActualizado = almacenService.actualizarAlmacen(id, almacenDTO);
            return ResponseEntity.ok(almacenActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un almacén
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        try {
            almacenService.eliminarAlmacen(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
