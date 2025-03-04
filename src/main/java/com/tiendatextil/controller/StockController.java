package com.tiendatextil.controller;

import com.tiendatextil.model.Stock;
import com.tiendatextil.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<Stock> obtenerStocks() {
        return stockService.obtenerStocks();
    }

    // Obtener un stock por id
    @GetMapping("/{id}")
    public ResponseEntity<Stock> obtenerStockPorId(@PathVariable Long id) {
        Optional<Stock> stock = stockService.obtenerStockPorId(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo stock
    @PostMapping
    public ResponseEntity<Stock> crearStock(@RequestBody Stock stock) {
        Stock nuevoStock = stockService.crearStock(stock);
        return new ResponseEntity<>(nuevoStock, HttpStatus.CREATED);
    }

    // Actualizar un stock
    @PutMapping("/{id}")
    public ResponseEntity<Stock> actualizarStock(@PathVariable Long id, @RequestBody Stock stock) {
        try {
            Stock stockActualizado = stockService.actualizarStock(id, stock);
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
