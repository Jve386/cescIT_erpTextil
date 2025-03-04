package com.tiendatextil.service;

import com.tiendatextil.model.Stock;
import com.tiendatextil.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // Crear un nuevo stock
    public Stock crearStock(Stock stock) {
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
