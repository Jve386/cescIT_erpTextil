package com.tiendatextil.service;

import com.tiendatextil.dto.*;
import com.tiendatextil.model.*;
import com.tiendatextil.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final AlmacenRepository almacenRepository;
    private final ArticuloRepository articuloRepository;
    private final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Autowired
    public StockService(StockRepository stockRepository, AlmacenRepository almacenRepository,
            ArticuloRepository articuloRepository) {
        this.stockRepository = stockRepository;
        this.almacenRepository = almacenRepository;
        this.articuloRepository = articuloRepository;
    }

    // Metodo para crear stock o sumarlo si ya existe
    public StockDTO crearStock(CrearStockDTO crearStockDTO) {
        logger.info("Creating stock for Articulo ID = {}, Almacen ID = {}",
                crearStockDTO.getIdArticulo(), crearStockDTO.getIdAlmacen());

        Optional<Almacen> almacenOpt = almacenRepository.findById(crearStockDTO.getIdAlmacen());
        Optional<Articulo> articuloOpt = articuloRepository.findById(crearStockDTO.getIdArticulo());

        if (almacenOpt.isEmpty() || articuloOpt.isEmpty()) {
            throw new IllegalArgumentException("Almacén o artículo no encontrado");
        }

        // Corrected parameter order
        Optional<Stock> existingStockOpt = stockRepository.findByArticulo_IdAndAlmacen_Id(
                crearStockDTO.getIdArticulo(), crearStockDTO.getIdAlmacen());

        if (existingStockOpt.isPresent()) {
            // Si el stock existe, actualizamos la cantidad sumando la nueva cantidad
            Stock existingStock = existingStockOpt.get();
            existingStock.setCantidad(existingStock.getCantidad() + crearStockDTO.getCantidad());
            stockRepository.save(existingStock);
            return new StockDTO(existingStock); // Devolvemos el DTO actualizado
        } else {
            // Si no existe, creamos un nuevo stock
            Stock stock = new Stock(articuloOpt.get(), almacenOpt.get(), crearStockDTO.getCantidad());
            stockRepository.save(stock);
            return new StockDTO(stock); // Devolvemos el DTO del nuevo stock
        }
    }

    // Método para buscar si un stock existe
    public Optional<Stock> buscarStockExistente(Long idAlmacen, Long idArticulo) {
        return stockRepository.findByArticulo_IdAndAlmacen_Id(idAlmacen, idArticulo);
    }

    // Método para sumar la cantidad de un stock existente
    public Stock sumarCantidadStock(Stock stockExistente, int cantidadAAgregar) {
        stockExistente.setCantidad(stockExistente.getCantidad() + cantidadAAgregar);
        return stockRepository.save(stockExistente); // Guardamos el stock actualizado
    }

    // Obtener todos los stocks
    public List<StockDTO> obtenerStocks() {
        return stockRepository.findAll().stream()
                .map(StockDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener un stock por ID
    public StockDTO obtenerStockPorId(Long id) {
        return stockRepository.findById(id)
                .map(StockDTO::new)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));
    }

    // Eliminar un stock
    public void eliminarStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Stock no encontrado");
        }
        stockRepository.deleteById(id);
    }

    // Crear stock desde DTO
    public Stock crearStockDesdeDTO(CrearStockDTO crearStockDTO) {
        Optional<Almacen> almacenOpt = almacenRepository.findById(crearStockDTO.getIdAlmacen());
        Optional<Articulo> articuloOpt = articuloRepository.findById(crearStockDTO.getIdArticulo());
    
        if (almacenOpt.isEmpty() || articuloOpt.isEmpty()) {
            throw new IllegalArgumentException("Almacén o artículo no encontrado");
        }
    
        Almacen almacen = almacenOpt.get();
        Articulo articulo = articuloOpt.get();
    
        Stock stock = new Stock(articulo, almacen, crearStockDTO.getCantidad());
        stockRepository.save(stock);
        return stock;
    }

    // Actualizar stock desde DTO
    public Stock actualizarStockDesdeDTO(Long id, StockDTO stockDTO) {
        Optional<Stock> stockOpt = stockRepository.findById(id);

        if (stockOpt.isEmpty()) {
            throw new RuntimeException("Stock no encontrado");
        }

        Stock stock = stockOpt.get();
        Optional<Almacen> almacenOpt = almacenRepository.findById(stockDTO.getIdAlmacen());
        Optional<Articulo> articuloOpt = articuloRepository.findById(stockDTO.getIdArticulo());

        if (almacenOpt.isEmpty() || articuloOpt.isEmpty()) {
            throw new IllegalArgumentException("Almacén o artículo no encontrado");
        }

        stock.setArticulo(articuloOpt.get());
        stock.setAlmacen(almacenOpt.get());
        stock.setCantidad(stockDTO.getCantidad());
        stockRepository.save(stock);

        return stock;
    }

    // Obtener stock por articulo
    public List<Stock> obtenerStockPorArticulo(Long articuloId) {
        return stockRepository.findByArticuloId(articuloId);
    }
}
