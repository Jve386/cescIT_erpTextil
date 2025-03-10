package com.tiendatextil.service;

import com.tiendatextil.dto.DetalleVentaDTO;
import com.tiendatextil.dto.VentaDTO;
import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.Cliente;
import com.tiendatextil.model.Almacen;
import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.model.Venta;
import com.tiendatextil.model.Stock;
import com.tiendatextil.repository.ArticuloRepository;
import com.tiendatextil.repository.VentaRepository;
import com.tiendatextil.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ArticuloRepository articuloRepository;
    private final StockRepository stockRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository, ArticuloRepository articuloRepository, StockRepository stockRepository) {
        this.ventaRepository = ventaRepository;
        this.articuloRepository = articuloRepository;
        this.stockRepository = stockRepository;
    }

    /**
     * Crea una nueva venta y la guarda en la base de datos.
     * @param ventaDTO Objeto DTO con la información de la venta.
     * @return La venta guardada.
     */
    @Transactional
    public VentaDTO crearVenta(VentaDTO ventaDTO) {
        // Crear la venta a partir del DTO
        Venta venta = new Venta();
        venta.setId(ventaDTO.getId());
    
        // Crear cliente y almacen a partir de los IDs del DTO
        Cliente cliente = new Cliente();
        cliente.setId(ventaDTO.getIdCliente());
        venta.setCliente(cliente);
    
        Almacen almacen = new Almacen();
        almacen.setId(ventaDTO.getIdAlmacen());
        venta.setAlmacen(almacen);
    
        // Establecer otros valores
        venta.setTotalSinIva(ventaDTO.getTotalSinIva());
        venta.setTotalConIva(ventaDTO.getTotalConIva());
        venta.setNumeroTicket(ventaDTO.getNumeroTicket());
        venta.setEstado(ventaDTO.getEstado());
        venta.setFecha(ventaDTO.getFecha());
    
        // Recorrer los detalles de venta del DTO y procesarlos
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetallesVenta()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId(detalleDTO.getId());
            detalle.setVenta(venta);
    
            // Establecer artículo a partir del ID del DTO
            Articulo articulo = new Articulo();
            articulo.setId(detalleDTO.getIdArticulo());
            detalle.setArticulo(articulo);
    
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setPrecioSinIva(detalleDTO.getPrecioSinIva());
            detalle.setIva(detalleDTO.getIva());
            detalle.setPrecioTotal(detalleDTO.getPrecioTotal());
    
            // Consultar artículo en la base de datos por su ID
            Optional<Articulo> articuloOpt = articuloRepository.findById(detalleDTO.getIdArticulo());
            if (articuloOpt.isPresent()) {
                articulo = articuloOpt.get();
                detalle.setPrecioUnitario(articulo.getPrecio());
            } else {
                throw new RuntimeException("Artículo no encontrado para el ID: " + detalleDTO.getIdArticulo());
            }
    
            // Agregar el detalle a la venta
            venta.getDetallesVenta().add(detalle);
    
            // Actualizar stock
            List<Stock> stocks = stockRepository.findByArticuloId(articulo.getId());
            if (stocks.isEmpty()) {
                throw new RuntimeException("Stock no encontrado para el artículo ID: " + articulo.getId());
            }
            
            // Agregar lógica adicional para manejar el stock si hay múltiples almacenes,
            // por ahora restamos del primer stock disponible.
            Stock stock = stocks.get(0);
    
            if (stock.getCantidad() < detalleDTO.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock para el artículo ID: " + articulo.getId());
            }
    
            stock.setCantidad(stock.getCantidad() - detalleDTO.getCantidad());
            stockRepository.save(stock);
        }
    
        // Guardar la venta en la base de datos
        Venta savedVenta = ventaRepository.save(venta);
    
        // Convertir la venta guardada a un DTO y retornarlo
        return convertToVentaDTO(savedVenta);
    }
    
    public VentaDTO actualizarVenta(Long id, VentaDTO ventaDTO) {
        if (ventaRepository.existsById(id)) {
            // Actualizar la venta con los nuevos datos
            Venta venta = new Venta();
            venta.setId(id);
            venta.setCliente(new Cliente());
            venta.getCliente().setId(ventaDTO.getIdCliente());
            venta.setAlmacen(new Almacen());
            venta.getAlmacen().setId(ventaDTO.getIdAlmacen());
            venta.setTotalSinIva(ventaDTO.getTotalSinIva());
            venta.setTotalConIva(ventaDTO.getTotalConIva());
            venta.setNumeroTicket(ventaDTO.getNumeroTicket());
            venta.setEstado(ventaDTO.getEstado());
            venta.setFecha(ventaDTO.getFecha());
    
            // Actualizar detalles de la venta
            for (DetalleVentaDTO detalleDTO : ventaDTO.getDetallesVenta()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setId(detalleDTO.getId());
                detalle.setVenta(venta);
    
                Articulo articulo = new Articulo();
                articulo.setId(detalleDTO.getIdArticulo());
                detalle.setArticulo(articulo);
    
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setPrecioSinIva(detalleDTO.getPrecioSinIva());
                detalle.setIva(detalleDTO.getIva());
                detalle.setPrecioTotal(detalleDTO.getPrecioTotal());
    
                Optional<Articulo> articuloOpt = articuloRepository.findById(detalleDTO.getIdArticulo());
                if (articuloOpt.isPresent()) {
                    articulo = articuloOpt.get();
                    detalle.setPrecioUnitario(articulo.getPrecio());
                } else {
                    throw new RuntimeException("Artículo no encontrado para el ID: " + detalleDTO.getIdArticulo());
                }
    
                // Actualizar stock
                List<Stock> stocks = stockRepository.findByArticuloId(articulo.getId());
                if (stocks.isEmpty()) {
                    throw new RuntimeException("Stock no encontrado para el artículo ID: " + articulo.getId());
                }
    
                Stock stock = stocks.get(0);
                if (stock.getCantidad() < detalleDTO.getCantidad()) {
                    throw new RuntimeException("No hay suficiente stock para el artículo ID: " + articulo.getId());
                }
    
                stock.setCantidad(stock.getCantidad() - detalleDTO.getCantidad());
                stockRepository.save(stock);
            }
    
            // Guardar la venta actualizada
            Venta updatedVenta = ventaRepository.save(venta);
    
            // Convertir la venta actualizada a un DTO y retornarlo
            return convertToVentaDTO(updatedVenta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    private VentaDTO convertToVentaDTO(Venta venta) {
        return new VentaDTO(
            venta.getId(),
            venta.getCliente().getId(),
            venta.getAlmacen().getId(),
            venta.getTotalSinIva(),
            venta.getTotalConIva(),
            venta.getNumeroTicket(),
            venta.getEstado(),
            venta.getFecha(),
            convertToDetalleVentaDTO(venta.getDetallesVenta())
        );
    }
    
    /**
     * Obtiene todas las ventas y las convierte en DTOs.
     * @return Lista de objetos VentaDTO.
     */
    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        List<VentaDTO> ventasDTO = new ArrayList<>();
        
        for (Venta venta : ventas) {
            VentaDTO ventaDTO = new VentaDTO(
                venta.getId(),
                venta.getCliente().getId(),
                venta.getAlmacen().getId(),
                venta.getTotalSinIva(),
                venta.getTotalConIva(),
                venta.getNumeroTicket(),
                venta.getEstado(),
                venta.getFecha(),
                convertToDetalleVentaDTO(venta.getDetallesVenta()) // Convertir detalles a DTO
            );
            ventasDTO.add(ventaDTO);
        }
        return ventasDTO;
    }

    /**
     * Obtiene una venta por su ID y la convierte a un DTO.
     * @param id ID de la venta.
     * @return Objeto Optional de VentaDTO.
     */
    public Optional<VentaDTO> obtenerVentaPorId(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(v -> new VentaDTO(
            v.getId(),
            v.getCliente().getId(),
            v.getAlmacen().getId(),
            v.getTotalSinIva(),
            v.getTotalConIva(),
            v.getNumeroTicket(),
            v.getEstado(),
            v.getFecha(),
            convertToDetalleVentaDTO(v.getDetallesVenta())
        ));
    }


    /**
     * Convierte una lista de Detalles de Venta a su correspondiente lista de DTOs.
     * @param detallesVenta Lista de objetos DetalleVenta.
     * @return Lista de objetos DetalleVentaDTO.
     */
    private List<DetalleVentaDTO> convertToDetalleVentaDTO(List<DetalleVenta> detallesVenta) {
        List<DetalleVentaDTO> detallesVentaDTO = new ArrayList<>();
        
        for (DetalleVenta detalle : detallesVenta) {
            DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO(
                detalle.getId(),
                detalle.getVenta().getId(),
                detalle.getArticulo().getId(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getPrecioSinIva(),
                detalle.getIva(),
                detalle.getPrecioTotal()
            );
            detallesVentaDTO.add(detalleVentaDTO);
        }
        
        return detallesVentaDTO;
    }

    /**
     * Elimina una venta por su ID.
     * @param id ID de la venta a eliminar.
     */
    public void eliminarVenta(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
}
