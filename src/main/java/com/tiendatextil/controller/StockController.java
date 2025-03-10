package com.tiendatextil.controller;

import com.tiendatextil.dto.StockDTO;
import com.tiendatextil.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Obtener todos los stocks
    @GetMapping
    public List<StockDTO> obtenerStocks() {
        return stockService.obtenerStocks();
    }

    // Obtener un stock por id
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> obtenerStockPorId(@PathVariable Long id) {
        return stockService.obtenerStockPorId(id)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo stock
    @PostMapping
    public ResponseEntity<StockDTO> crearStock(@RequestBody StockDTO stockDTO) {
        StockDTO nuevoStockDTO = stockService.crearStock(stockDTO);
        return new ResponseEntity<>(nuevoStockDTO, HttpStatus.CREATED);
    }

    // Actualizar un stock
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> actualizarStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        try {
            StockDTO stockActualizado = stockService.actualizarStock(id, stockDTO);
            return ResponseEntity.ok(stockActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un stock
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarStock(@PathVariable Long id) {
        try {
            stockService.eliminarStock(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
