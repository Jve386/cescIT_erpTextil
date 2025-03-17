package com.tiendatextil.service;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.model.Stock;
import com.tiendatextil.model.Venta;
import com.tiendatextil.repository.ArticuloRepository;
import com.tiendatextil.repository.StockRepository;
import com.tiendatextil.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import com.tiendatextil.dto.VentaDTO;
import com.tiendatextil.dto.DetalleVentaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);
    private final ObjectMapper objectMapper;
    private final VentaRepository ventaRepository;
    private final ArticuloRepository articuloRepository;
    private final StockRepository stockRepository;
    private final ArticuloService articuloService;
    

    @Autowired
    public VentaService(VentaRepository ventaRepository, ArticuloRepository articuloRepository,
            StockRepository stockRepository, ArticuloService articuloService) {
        this.ventaRepository = ventaRepository;
        this.articuloRepository = articuloRepository;
        this.stockRepository = stockRepository;
        this.articuloService = articuloService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear una nueva venta
    @Transactional
    public Venta crearVenta(Venta venta) {
        try {
            logger.info("Creating new sale with {} items", venta.getDetallesVenta().size());
            
            // Set the current date if not already set
            if (venta.getFecha() == null) {
                venta.setFecha(new Date());
            }
            
            // Calculate totals and process each detail
            double totalSinIva = 0;
            double totalConIva = 0;
            
            for (DetalleVenta detalle : venta.getDetallesVenta()) {
                // Get the full article entity if only ID is provided
                if (detalle.getArticulo() != null && detalle.getArticulo().getId() != null) {
                    Articulo articulo = articuloRepository.findById(detalle.getArticulo().getId())
                        .orElseThrow(() -> new RuntimeException("Article not found with ID: " + detalle.getArticulo().getId()));
                    
                    // Set the complete article in the detail
                    detalle.setArticulo(articulo);
                    
                    // Use the sale price from the article
                    Double precioVenta = articulo.getPrecioVenta();
                    if (precioVenta == null) {
                        throw new RuntimeException("Article " + articulo.getId() + " has no sale price defined");
                    }
                    
                    // Set the unit price from the article's sale price
                    detalle.setPrecioUnitario(precioVenta);
                    
                    // Calculate subtotal (price without VAT)
                    double precioSinIva = precioVenta * detalle.getCantidad();
                    detalle.setPrecioSinIva(precioSinIva);
                    
                    // Calculate VAT (21%)
                    double iva = precioSinIva * 0.21;
                    detalle.setIva(iva);
                    
                    // Calculate total price (with VAT)
                    double precioTotal = precioSinIva + iva;
                    detalle.setPrecioTotal(precioTotal);
                    
                    // Add to sale totals
                    totalSinIva += precioSinIva;
                    totalConIva += precioTotal;
                    
                    // Update stock
                    Stock stock = stockRepository.findByArticulo_IdAndAlmacen_Id(articulo.getId(), venta.getAlmacen().getId())
                        .orElseThrow(() -> new RuntimeException("No stock found for article " + articulo.getId() + 
                                                             " in warehouse " + venta.getAlmacen().getId()));
                    
                    if (stock.getCantidad() < detalle.getCantidad()) {
                        throw new RuntimeException("Insufficient stock for article " + articulo.getId() + 
                                                ". Available: " + stock.getCantidad() + ", Requested: " + detalle.getCantidad());
                    }
                    
                    // Reduce stock quantity
                    stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                    stockRepository.save(stock);
                    
                    // Set the venta reference in the detail
                    detalle.setVenta(venta);
                    
                    logger.debug("Processed detail - Article: {}, Quantity: {}, Unit Price: {}, Subtotal: {}, Total: {}", 
                               articulo.getId(), detalle.getCantidad(), precioVenta, precioSinIva, precioTotal);
                } else {
                    throw new RuntimeException("Sale detail is missing article information");
                }
            }
            
            // Set the calculated totals in the sale
            venta.setTotalSinIva(totalSinIva);
            venta.setTotalConIva(totalConIva);
            
            logger.info("Sale created successfully with total (without VAT): {}, total (with VAT): {}", 
                      totalSinIva, totalConIva);
            
            return ventaRepository.save(venta);
        } catch (Exception e) {
            logger.error("Error creating sale", e);
            throw new RuntimeException("Error creating sale: " + e.getMessage(), e);
        }
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por su ID
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // Actualizar una venta
    public Venta actualizarVenta(Long id, Venta venta) {
        if (ventaRepository.existsById(id)) {
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }

    // Eliminar una venta
    @Transactional
    public void eliminarVenta(Long id) {
        logger.info("Iniciando eliminación de venta con ID: {}", id);
        
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (!ventaOpt.isPresent()) {
            String errorMsg = "Venta no encontrada con ID: " + id;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        Venta venta = ventaOpt.get();
        
        // Si la venta está completada, restaurar el stock
        if ("completada".equals(venta.getEstado())) {
            logger.info("Venta completada, restaurando stock para {} artículos", venta.getDetallesVenta().size());
            
            for (DetalleVenta detalle : venta.getDetallesVenta()) {
                Articulo articulo = detalle.getArticulo();
                if (articulo == null) {
                    String errorMsg = "Detalle de venta sin artículo asociado";
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                
                Long articuloId = articulo.getId();
                Long almacenId = venta.getAlmacen().getId();
                logger.info("Restaurando stock para artículo ID: {} en almacén ID: {}", articuloId, almacenId);
                
                Optional<Stock> stockOpt = stockRepository.findByArticulo_IdAndAlmacen_Id(articuloId, almacenId);
                
                if (!stockOpt.isPresent()) {
                    String errorMsg = "No se encontró el stock para el artículo: " + articulo.getProducto().getNombre() + 
                                     " en el almacén ID: " + almacenId;
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                
                Stock stock = stockOpt.get();
                // Restaurar la cantidad vendida al stock
                stock.setCantidad(stock.getCantidad() + detalle.getCantidad());
                stockRepository.save(stock);
                logger.info("Stock restaurado para el artículo: {}. Nueva cantidad: {}", articulo, stock.getCantidad());
            }
        } else {
            logger.info("Venta en estado 'pendiente', no es necesario restaurar stock");
        }
        
        // Eliminar la venta
        ventaRepository.deleteById(id);
        logger.info("Venta eliminada exitosamente");
    }

    /**
     * Obtiene todas las ventas como DTOs
     */
    public List<VentaDTO> obtenerVentasDTO() {
        List<Venta> ventas = ventaRepository.findAll();
        // Serialize and log ventas
        try {
            String json = objectMapper.writeValueAsString(ventas);
            System.out.println("Serialized Ventas: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ventas.stream()
                .map(this::convertToVentaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una venta por ID como DTO
     */
    public Optional<VentaDTO> obtenerVentaDTOPorId(Long id) {
        return ventaRepository.findById(id)
                .map(this::convertToVentaDTO);
    }

    /**
     * Convierte una Venta a VentaDTO
     */
    private VentaDTO convertToVentaDTO(Venta venta) {
        System.out.println("Converting Venta: " + venta.getId()); 
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        if (venta.getCliente() != null) {
            dto.setIdCliente(venta.getCliente().getId());
            dto.setNombreCliente(venta.getCliente().getNombre());
        }
        if (venta.getAlmacen() != null) {
            dto.setIdAlmacen(venta.getAlmacen().getId());
            dto.setNombreAlmacen(venta.getAlmacen().getNombre());
        }
        dto.setFecha(venta.getFecha() != null ? venta.getFecha().toString() : null);
        dto.setTotalSinIVA(venta.getTotalSinIva());
        dto.setTotalConIVA(venta.getTotalConIva());
        dto.setNumeroTicket(venta.getNumeroTicket());
        dto.setEstado(venta.getEstado());

        if (venta.getDetallesVenta() != null) {
            List<DetalleVentaDTO> detallesDTO = venta.getDetallesVenta().stream()
                    .map(this::convertToDetalleVentaDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        // Log the populated DTO
        System.out.println("Converted VentaDTO: " + dto);

        return dto;
    }

    /**
     * Convierte un DetalleVenta a DetalleVentaDTO
     */
    private DetalleVentaDTO convertToDetalleVentaDTO(DetalleVenta detalle) {
        System.out.println("Converting DetalleVenta: " + detalle.getId()); 
        DetalleVentaDTO dto = new DetalleVentaDTO();
        if (detalle.getArticulo() != null) {
            dto.setIdArticulo(detalle.getArticulo().getId());
            if (detalle.getArticulo().getProducto() != null) {
                dto.setNombreProducto(detalle.getArticulo().getProducto().getNombre());
            }
            if (detalle.getArticulo().getTalla() != null) {
                dto.setTalla(detalle.getArticulo().getTalla().getTalla());
            }
            if (detalle.getArticulo().getColor() != null) {
                dto.setColor(detalle.getArticulo().getColor().getColor());
            }
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setPrecioTotal(detalle.getPrecioTotal());

        return dto;
    }

    // Actualizar una venta de pendiente a completada
    @Transactional
    public Venta completarVenta(Long id) {
        logger.info("Completando venta con ID: {}", id);
        
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (!ventaOpt.isPresent()) {
            String errorMsg = "Venta no encontrada con ID: " + id;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        Venta venta = ventaOpt.get();
        
        // Verificar que la venta esté en estado pendiente
        if (!"pendiente".equals(venta.getEstado())) {
            String errorMsg = "Solo se pueden completar ventas en estado 'pendiente'. Estado actual: " + venta.getEstado();
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        // Actualizar el stock para cada detalle de la venta
        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            Articulo articulo = detalle.getArticulo();
            if (articulo == null) {
                String errorMsg = "Detalle de venta sin artículo asociado";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            Long articuloId = articulo.getId();
            Long almacenId = venta.getAlmacen().getId();
            logger.info("Verificando stock para artículo ID: {} en almacén ID: {}", articuloId, almacenId);
            
            Optional<Stock> stockOpt = stockRepository.findByArticulo_IdAndAlmacen_Id(articuloId, almacenId);
            
            if (!stockOpt.isPresent()) {
                String errorMsg = "No hay stock disponible para el artículo: " + articulo.getProducto().getNombre() + 
                                 " en el almacén ID: " + almacenId;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            Stock stock = stockOpt.get();
            if (stock.getCantidad() < detalle.getCantidad()) {
                String errorMsg = "No hay suficiente stock para el artículo: " + articulo.getProducto().getNombre() + 
                                 " en el almacén ID: " + almacenId + 
                                 ". Disponible: " + stock.getCantidad() + 
                                 ", Solicitado: " + detalle.getCantidad();
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            // Actualizar stock
            stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
            stockRepository.save(stock);
            logger.info("Stock actualizado para el artículo: {}. Nueva cantidad: {}", articulo, stock.getCantidad());
        }
        
        // Actualizar estado de la venta
        venta.setEstado("completada");
        
        try {
            Venta ventaActualizada = ventaRepository.save(venta);
            logger.info("Venta completada exitosamente: {}", ventaActualizada);
            return ventaActualizada;
        } catch (Exception e) {
            String errorMsg = "Error al completar la venta en la base de datos: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private Articulo obtenerArticulo(String nombreProducto, String talla, String color) {
        Optional<Articulo> articuloOpt = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor(
                nombreProducto, talla, color);
        
        if (articuloOpt.isEmpty()) {
            throw new RuntimeException("Article not found with product: " + nombreProducto + 
                                     ", size: " + talla + ", color: " + color);
        }
        
        return articuloOpt.get();
    }

}
