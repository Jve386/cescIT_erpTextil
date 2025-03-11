package com.tiendatextil.service;

import com.tiendatextil.model.Stock;
import com.tiendatextil.model.Almacen;
import com.tiendatextil.repository.StockRepository;
import com.tiendatextil.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final AlmacenRepository almacenRepository;

    @Autowired
    public StockService(StockRepository stockRepository, AlmacenRepository almacenRepository) {
        this.stockRepository = stockRepository;
        this.almacenRepository = almacenRepository;
    }

    // Crear un nuevo stock
    public Stock crearStock(Stock stock) {
        if (stock.getAlmacen() == null) {
            throw new IllegalArgumentException("El almacén no puede ser nulo");
        }

        // Verificar que el almacén realmente exista
        Optional<Almacen> almacenOptional = almacenRepository.findById(stock.getAlmacen().getId());
        if (!almacenOptional.isPresent()) {
            throw new IllegalArgumentException("Almacén no encontrado");
        }

        return stockRepository.save(stock);
    }

    // Obtener todos los stocks
    public List<Stock> obtenerStocks() {
        return stockRepository.findAll();
    }

    // Obtener un stock por su ID
    public Optional<Stock> obtenerStockPorId(Long id) {
        return stockRepository.findById(id);
    }

    public Stock actualizarStock(Long id, Stock stock) {
        if (stockRepository.existsById(id)) {
            return stockRepository.save(stock);
        } else {
            throw new RuntimeException("Stock no encontrado");
        }
    }

    // Eliminar un stock
    public void eliminarStock(Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
        } else {
            throw new RuntimeException("Stock no encontrado");
        }
    }
}
