package com.tiendatextil.controller;

import com.tiendatextil.dto.CrearStockDTO;
import com.tiendatextil.dto.StockDTO;
import com.tiendatextil.model.Stock;
import com.tiendatextil.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Obtener todos los stocks en formato DTO
    @GetMapping
    public ResponseEntity<List<StockDTO>> obtenerStocks() {
        List<StockDTO> stockDTOs = stockService.obtenerStocks();  //  devuelve una lista de StockDTO
        return ResponseEntity.ok(stockDTOs);
    }

    // Obtener un stock por ID y devolver DTO
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> obtenerStockPorId(@PathVariable Long id) {
        StockDTO stockDTO = stockService.obtenerStockPorId(id);
        return ResponseEntity.ok(stockDTO);
    }
    
    //Obtener un stock por articuloID
    @GetMapping("/articulo/{id}")
    public ResponseEntity<List<StockDTO>> obtenerStockPorArticuloId(@PathVariable Long id) {
        List<Stock> stocks = stockService.obtenerStockPorArticulo(id);

        // Convertir los stocks a StockDTO
        List<StockDTO> stockDTOs = stocks.stream()
                .map(StockDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stockDTOs);
    }

    // Crear un nuevo stock desde DTO
    @PostMapping
    public ResponseEntity<StockDTO> crearStock(@RequestBody CrearStockDTO crearStockDTO) {
        logger.info("Datos recibidos: {}", crearStockDTO);
        try {
            // Verificar si el stock ya existe
            Optional<Stock> stockExistente = stockService.buscarStockExistente(crearStockDTO.getIdArticulo(), crearStockDTO.getIdAlmacen());
    
            if (stockExistente.isPresent()) {
                // Si el stock ya existe, se actualiza (sumamos la cantidad)
                Stock stockActualizado = stockService.sumarCantidadStock(stockExistente.get(), crearStockDTO.getCantidad());
                return ResponseEntity.ok(new StockDTO(stockActualizado));  // Retorna el stock actualizado
            } else {
                // Si no existe, se crea un nuevo stock
                Stock nuevoStock = stockService.crearStockDesdeDTO(crearStockDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(new StockDTO(nuevoStock));  // Retorna el nuevo stock creado
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: ", e);
            return ResponseEntity.badRequest().body(null);  // Error 400 si algo falla
        } catch (Exception e) {
            logger.error("Error creating stock: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Error 500 for other exceptions
        }
    }



    // Actualizar un stock desde DTO
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> actualizarStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        try {
            Stock stockActualizado = stockService.actualizarStockDesdeDTO(id, stockDTO);
            return ResponseEntity.ok(new StockDTO(stockActualizado));
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
