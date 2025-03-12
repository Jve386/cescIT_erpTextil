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

@Service
public class VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);
    private final ObjectMapper objectMapper;
    private final VentaRepository ventaRepository;
    private final ArticuloRepository articuloRepository;
    private final StockRepository stockRepository;
    

    @Autowired
    public VentaService(VentaRepository ventaRepository, ArticuloRepository articuloRepository,
            StockRepository stockRepository) {
        this.ventaRepository = ventaRepository;
        this.articuloRepository = articuloRepository;
        this.stockRepository = stockRepository;
        this.objectMapper = new ObjectMapper();
    }

    // Crear una nueva venta
    @Transactional
    public Venta crearVenta(Venta venta) {
        logger.info("Iniciando creación de venta con {} artículos y estado: {}", 
                    venta.getDetallesVenta().size(), venta.getEstado());
        
        // Validar que la venta tenga detalles
        if (venta.getDetallesVenta() == null || venta.getDetallesVenta().isEmpty()) {
            String errorMsg = "No se puede crear una venta sin artículos";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        // Validar que el cliente exista
        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            String errorMsg = "El cliente es requerido para crear una venta";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        // Validar que el almacén exista
        if (venta.getAlmacen() == null || venta.getAlmacen().getId() == null) {
            String errorMsg = "El almacén es requerido para crear una venta";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        // Validar que el estado sea válido
        if (venta.getEstado() == null) {
            venta.setEstado("pendiente"); // Estado por defecto
        } else if (!venta.getEstado().equals("pendiente") && !venta.getEstado().equals("completada")) {
            String errorMsg = "El estado de la venta debe ser 'pendiente' o 'completada'";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        double totalSinIva = 0;
        double totalConIva = 0;

        // Determinar si debemos actualizar el stock basado en el estado de la venta
        boolean actualizarStock = "completada".equals(venta.getEstado());
        logger.info("¿Actualizar stock? {}", actualizarStock);

        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            logger.info("Procesando detalle de venta: {}", detalle);
            
            // Validar que el artículo tenga toda la información necesaria
            if (detalle.getArticulo() == null) {
                String errorMsg = "El artículo es requerido para el detalle de venta";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            if (detalle.getArticulo().getProducto() == null || 
                detalle.getArticulo().getTalla() == null || 
                detalle.getArticulo().getColor() == null) {
                String errorMsg = "El artículo debe tener producto, talla y color";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // Buscar artículo
            String nombreProducto = detalle.getArticulo().getProducto().getNombre();
            String talla = detalle.getArticulo().getTalla().getTalla();
            String color = detalle.getArticulo().getColor().getColor();
            
            logger.info("Buscando artículo con nombre: {}, talla: {}, color: {}", nombreProducto, talla, color);
            
            Optional<Articulo> articuloOpt = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor(
                    nombreProducto, talla, color);

            if (!articuloOpt.isPresent()) {
                String errorMsg = "Artículo no encontrado con nombre: " + nombreProducto + 
                                 ", talla: " + talla + ", color: " + color;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            Articulo articulo = articuloOpt.get();
            logger.info("Artículo encontrado: {}", articulo);

            // Verificar stock solo si la venta está completada
            if (actualizarStock) {
                Long articuloId = articulo.getId();
                Long almacenId = venta.getAlmacen().getId();
                logger.info("Verificando stock para artículo ID: {} en almacén ID: {}", articuloId, almacenId);
                
                Optional<Stock> stockOpt = stockRepository.findByArticulo_IdAndAlmacen_Id(articuloId, almacenId);

                if (!stockOpt.isPresent()) {
                    String errorMsg = "No hay stock disponible para el artículo: " + nombreProducto + 
                                     " en el almacén ID: " + almacenId;
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                
                Stock stock = stockOpt.get();
                if (stock.getCantidad() < detalle.getCantidad()) {
                    String errorMsg = "No hay suficiente stock para el artículo: " + nombreProducto + 
                                     " en el almacén ID: " + almacenId + 
                                     ". Disponible: " + stock.getCantidad() + 
                                     ", Solicitado: " + detalle.getCantidad();
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }

                // Actualizar stock solo si la venta está completada
                stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                stockRepository.save(stock);
                logger.info("Stock actualizado para el artículo: {}. Nueva cantidad: {}", articulo, stock.getCantidad());
            } else {
                logger.info("Venta en estado 'pendiente', no se actualiza el stock para el artículo: {}", articulo);
            }

            // Calcular precios
            double precioSinIva = detalle.getPrecioUnitario() * detalle.getCantidad();
            double iva = precioSinIva * 0.21;
            double precioTotal = precioSinIva + iva;

            // Actualizar detalle
            detalle.setPrecioSinIva(precioSinIva);
            detalle.setIva(iva);
            detalle.setPrecioTotal(precioTotal);
            detalle.setArticulo(articulo);
            detalle.setVenta(venta);

            // Acumular totales
            totalSinIva += precioSinIva;
            totalConIva += precioTotal;
        }

        // Guardar venta
        venta.setTotalSinIva(totalSinIva);
        venta.setTotalConIva(totalConIva);
        
        try {
            Venta ventaGuardada = ventaRepository.save(venta);
            logger.info("Venta guardada exitosamente con estado '{}': {}", ventaGuardada.getEstado(), ventaGuardada);
            return ventaGuardada;
        } catch (Exception e) {
            String errorMsg = "Error al guardar la venta en la base de datos: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
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
    public void eliminarVenta(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
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

}
