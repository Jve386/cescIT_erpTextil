package com.tiendatextil.service;

import com.tiendatextil.dto.StockDTO;
import com.tiendatextil.model.Stock;
import com.tiendatextil.model.Articulo;  // Asegúrate de que esta clase esté importada
import com.tiendatextil.model.Almacen;   // Asegúrate de que esta clase esté importada
import com.tiendatextil.repository.StockRepository;
import com.tiendatextil.repository.ArticuloRepository;  // Repositorio para Articulo
import com.tiendatextil.repository.AlmacenRepository;   // Repositorio para Almacen
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ArticuloRepository articuloRepository;  // Inyectamos el repositorio de Articulo
    private final AlmacenRepository almacenRepository;    // Inyectamos el repositorio de Almacen

    @Autowired
    public StockService(StockRepository stockRepository,
                        ArticuloRepository articuloRepository,
                        AlmacenRepository almacenRepository) {
        this.stockRepository = stockRepository;
        this.articuloRepository = articuloRepository;
        this.almacenRepository = almacenRepository;
    }

    // Crear un nuevo stock
    public StockDTO crearStock(StockDTO stockDTO) {
        Stock stock = convertirAStock(stockDTO);
        Stock nuevoStock = stockRepository.save(stock);
        return convertirAStockDTO(nuevoStock);
    }

    // Obtener todos los stocks
    public List<StockDTO> obtenerStocks() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                     .map(this::convertirAStockDTO)
                     .collect(Collectors.toList());
    }

    // Obtener un stock por su ID
    public Optional<StockDTO> obtenerStockPorId(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.map(this::convertirAStockDTO);
    }

    // Actualizar un stock
    public StockDTO actualizarStock(Long id, StockDTO stockDTO) {
        Stock stock = convertirAStock(stockDTO);
        stock.setId(id);
        Stock stockActualizado = stockRepository.save(stock);
        return convertirAStockDTO(stockActualizado);
    }

    // Eliminar un stock
    public void eliminarStock(Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
        } else {
            throw new RuntimeException("Stock no encontrado");
        }
    }

    // Conversión de StockDTO a Stock
    private Stock convertirAStock(StockDTO stockDTO) {
        // Aquí estamos buscando los objetos Articulo y Almacen por su ID
        Articulo articulo = articuloRepository.findById(stockDTO.getIdArticulo())
                                              .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));
        Almacen almacen = almacenRepository.findById(stockDTO.getIdAlmacen())
                                           .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));

        return new Stock(articulo, almacen, stockDTO.getCantidad());
    }

    // Conversión de Stock a StockDTO
    private StockDTO convertirAStockDTO(Stock stock) {
        return new StockDTO(
            stock.getId(),
            stock.getArticulo().getId(),
            stock.getAlmacen().getId(),
            stock.getCantidad()
        );
    }
}
