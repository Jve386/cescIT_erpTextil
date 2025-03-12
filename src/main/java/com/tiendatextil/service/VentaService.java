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
        logger.info("Iniciando creación de venta con {} artículos", venta.getDetallesVenta().size());

        double totalSinIva = 0;
        double totalConIva = 0;

        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            logger.info("Procesando detalle de venta: {}", detalle);

            // Buscar artículo
            Optional<Articulo> articuloOpt = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor(
                    detalle.getArticulo().getProducto().getNombre(),
                    detalle.getArticulo().getTalla().getTalla(),
                    detalle.getArticulo().getColor().getColor());

            if (!articuloOpt.isPresent()) {
                logger.error("Artículo no encontrado: {}", detalle.getArticulo());
                throw new RuntimeException("Artículo no encontrado");
            }

            Articulo articulo = articuloOpt.get();
            logger.info("Artículo encontrado: {}", articulo);

            // Verificar stock
            Optional<Stock> stockOpt = stockRepository.findByArticulo_IdAndAlmacen_Id(
                    articulo.getId(),
                    venta.getAlmacen().getId());

            if (!stockOpt.isPresent() || stockOpt.get().getCantidad() < detalle.getCantidad()) {
                logger.error("No hay suficiente stock para el artículo: {}", articulo);
                throw new RuntimeException("No hay suficiente stock");
            }

            // Actualizar stock
            Stock stock = stockOpt.get();
            stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
            stockRepository.save(stock);
            logger.info("Stock actualizado para el artículo: {}", articulo);

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

            // Asegurar de que el detalle se asocie correctamente a la venta
            venta.getDetallesVenta().add(detalle);
        }

        // Guardar venta
        venta.setTotalSinIva(totalSinIva);
        venta.setTotalConIva(totalConIva);
        Venta ventaGuardada = ventaRepository.save(venta);
        logger.info("Venta guardada exitosamente: {}", ventaGuardada);
        return ventaGuardada;
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

}
