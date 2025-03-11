package com.tiendatextil.service;

import com.tiendatextil.model.Articulo;
import com.tiendatextil.model.DetalleVenta;
import com.tiendatextil.model.Stock;
import com.tiendatextil.model.Venta;
import com.tiendatextil.repository.ArticuloRepository;
import com.tiendatextil.repository.StockRepository;
import com.tiendatextil.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Crear una nueva venta
    public Venta crearVenta(Venta venta) {
        // Para cada detalle de venta, obtenemos el artículo, calculamos el precio y actualizamos el stock
        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            // Consultar el artículo utilizando nombre del producto, talla y color
            Optional<Articulo> articuloOpt = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor(
                    detalle.getArticulo().getProducto().getNombre(),
                    detalle.getArticulo().getTalla().getTalla(),
                    detalle.getArticulo().getColor().getColor()
            );

            if (articuloOpt.isPresent()) {
                Articulo articulo = articuloOpt.get();

                // Establecer el precio unitario del detalle de venta con el precio del artículo
                detalle.setPrecioUnitario(articulo.getPrecio());

                // Calcular el precio total sin IVA
                double precioSinIva = detalle.getPrecioUnitario() * detalle.getCantidad();
                detalle.setPrecioSinIva(precioSinIva);

                // Calcular el IVA (21% por ejemplo)
                double iva = precioSinIva * 0.21;  // Si es 21% de IVA
                detalle.setIva(iva);

                // Calcular el precio total (sin IVA + IVA)
                detalle.setPrecioTotal(precioSinIva + iva);

                // Obtener el stock del artículo
                List<Stock> stocks = stockRepository.findByArticuloId(articulo.getId()); // Buscar por ID del artículo

                if (stocks.isEmpty()) {
                    throw new RuntimeException("Stock no encontrado para el artículo " + articulo.getId());
                }

                // Aquí puedes agregar lógica adicional para manejar el stock si hay múltiples almacenes,
                // por ahora restamos del primer stock disponible.
                Stock stock = stocks.get(0); // Obtener el primer stock disponible

                if (stock.getCantidad() < detalle.getCantidad()) {
                    throw new RuntimeException("No hay suficiente stock para el artículo " + articulo.getId());
                }

                // Actualizar el stock: restamos la cantidad vendida
                stock.setCantidad(stock.getCantidad() - detalle.getCantidad());
                stockRepository.save(stock); // Guardar el stock actualizado
            }
        }

        // Guardar la venta y retornar la venta creada
        return ventaRepository.save(venta);
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
}
