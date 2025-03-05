// DataLoader
package com.tiendatextil.config;

import com.tiendatextil.model.*;
import com.tiendatextil.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final TallaRepository tallaRepository;
    private final ColorRepository colorRepository;
    private final TipoAlmacenRepository tipoAlmacenRepository;
    private final AlmacenRepository almacenRepository;
    private final ClienteRepository clienteRepository;
    private final ArticuloRepository articuloRepository;
    private final StockRepository stockRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public DataLoader(
            RolRepository rolRepository,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            TallaRepository tallaRepository,
            ColorRepository colorRepository,
            TipoAlmacenRepository tipoAlmacenRepository,
            AlmacenRepository almacenRepository,
            ClienteRepository clienteRepository,
            ArticuloRepository articuloRepository,
            StockRepository stockRepository,
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository) {
        this.rolRepository = rolRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
        this.colorRepository = colorRepository;
        this.tipoAlmacenRepository = tipoAlmacenRepository;
        this.almacenRepository = almacenRepository;
        this.clienteRepository = clienteRepository;
        this.articuloRepository = articuloRepository;
        this.stockRepository = stockRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Iniciando carga de datos...");

//            // Limpiar datos anteriores
//            detalleVentaRepository.deleteAll();
//            ventaRepository.deleteAll();
//            stockRepository.deleteAll();
//            articuloRepository.deleteAll();
//            productoRepository.deleteAll();
//            categoriaRepository.deleteAll();
//            rolRepository.deleteAll();

            // Insertar datos de ejemplo en la tabla de roles
            if (rolRepository.count() == 0) {
                System.out.println("Insertando roles...");
                saveIfNotExists(rolRepository, new Rol("Administrador"), r -> r.getNombre().equals("Administrador"));
                saveIfNotExists(rolRepository, new Rol("Vendedor"), r -> r.getNombre().equals("Vendedor"));
            }

            // Insertar datos de ejemplo en la tabla de categorías
            if (categoriaRepository.count() == 0) {
                saveIfNotExists(categoriaRepository, new Categoria("Camisetas"), c -> c.getNombre().equals("Camisetas"));
                saveIfNotExists(categoriaRepository, new Categoria("Pantalones"), c -> c.getNombre().equals("Pantalones"));
                saveIfNotExists(categoriaRepository, new Categoria("Zapatos"), c -> c.getNombre().equals("Zapatos"));
            }

            // Insertar datos de ejemplo en la tabla de tallas
            if (tallaRepository.count() == 0) {
                saveIfNotExists(tallaRepository, new Talla("S"), t -> t.getTalla().equals("S"));
                saveIfNotExists(tallaRepository, new Talla("M"), t -> t.getTalla().equals("M"));
                saveIfNotExists(tallaRepository, new Talla("L"), t -> t.getTalla().equals("L"));
                saveIfNotExists(tallaRepository, new Talla("XL"), t -> t.getTalla().equals("XL"));
            }

            // Insertar datos de ejemplo en la tabla de colores
            if (colorRepository.count() == 0) {
                saveIfNotExists(colorRepository, new Color("Rojo"), c -> c.getColor().equals("Rojo"));
                saveIfNotExists(colorRepository, new Color("Azul"), c -> c.getColor().equals("Azul"));
                saveIfNotExists(colorRepository, new Color("Negro"), c -> c.getColor().equals("Negro"));
                saveIfNotExists(colorRepository, new Color("Blanco"), c -> c.getColor().equals("Blanco"));
            }

            // Insertar datos de ejemplo en la tabla de tipo de almacenes
            if (tipoAlmacenRepository.count() == 0) {
                saveIfNotExists(tipoAlmacenRepository, new TipoAlmacen("Tienda"), t -> t.getTipo().equals("Tienda"));
                saveIfNotExists(tipoAlmacenRepository, new TipoAlmacen("Almacén"), t -> t.getTipo().equals("Almacén"));
            }

            // Insertar almacenes
            if (almacenRepository.count() == 0) {
                TipoAlmacen tienda = tipoAlmacenRepository.findByTipo("Tienda").orElseThrow();
                TipoAlmacen almacen = tipoAlmacenRepository.findByTipo("Almacén").orElseThrow();

                saveIfNotExists(almacenRepository, new Almacen("Tienda Central", "Calle Principal 123", tienda), a -> a.getNombre().equals("Tienda Central"));
                saveIfNotExists(almacenRepository, new Almacen("Almacén Norte", "Avenida Industrial 456", almacen), a -> a.getNombre().equals("Almacén Norte"));
            }

            // Insertar clientes
            if (clienteRepository.count() == 0) {
                saveIfNotExists(clienteRepository, new Cliente("Juan Pérez", "juan@example.com", "123456789"), c -> c.getNombre().equals("Juan Pérez"));
                saveIfNotExists(clienteRepository, new Cliente("María López", "maria@example.com", "987654321"), c -> c.getNombre().equals("María López"));
            }

            // Insertar productos
            if (productoRepository.count() == 0) {
                Categoria camisetas = categoriaRepository.findByNombre("Camisetas").orElseThrow();
                Categoria pantalones = categoriaRepository.findByNombre("Pantalones").orElseThrow();
                Categoria zapatos = categoriaRepository.findByNombre("Zapatos").orElseThrow();

                saveIfNotExists(productoRepository, new Producto("Camiseta Básica", "Camiseta de algodón", 9.99, camisetas), p -> p.getNombre().equals("Camiseta Básica"));
                saveIfNotExists(productoRepository, new Producto("Pantalón Vaquero", "Pantalón de mezclilla", 29.99, pantalones), p -> p.getNombre().equals("Pantalón Vaquero"));
                saveIfNotExists(productoRepository, new Producto("Zapatillas Deportivas", "Zapatillas para correr", 49.99, zapatos), p -> p.getNombre().equals("Zapatillas Deportivas"));
            }

            // Insertar artículos (combinación de producto, talla y color)
            if (articuloRepository.count() == 0) {
                Producto camiseta = productoRepository.findByNombre("Camiseta Básica").orElseThrow();
                Producto pantalon = productoRepository.findByNombre("Pantalón Vaquero").orElseThrow();
                Producto zapatillas = productoRepository.findByNombre("Zapatillas Deportivas").orElseThrow();

                Talla s = tallaRepository.findByTalla("S").orElseThrow();
                Talla m = tallaRepository.findByTalla("M").orElseThrow();
                Talla l = tallaRepository.findByTalla("L").orElseThrow();
                Talla xl = tallaRepository.findByTalla("XL").orElseThrow();

                Color rojo = colorRepository.findByColor("Rojo").orElseThrow();
                Color azul = colorRepository.findByColor("Azul").orElseThrow();
                Color negro = colorRepository.findByColor("Negro").orElseThrow();
                Color blanco = colorRepository.findByColor("Blanco").orElseThrow();

                saveIfNotExists(articuloRepository, new Articulo(camiseta, s, rojo, 5.00), a -> a.getProducto().getNombre().equals("Camiseta Básica") && a.getTalla().getTalla().equals("S") && a.getColor().getColor().equals("Rojo"));
                saveIfNotExists(articuloRepository, new Articulo(camiseta, m, azul, 5.50), a -> a.getProducto().getNombre().equals("Camiseta Básica") && a.getTalla().getTalla().equals("M") && a.getColor().getColor().equals("Azul"));
                saveIfNotExists(articuloRepository, new Articulo(pantalon, l, negro, 15.00), a -> a.getProducto().getNombre().equals("Pantalón Vaquero") && a.getTalla().getTalla().equals("L") && a.getColor().getColor().equals("Negro"));
                saveIfNotExists(articuloRepository, new Articulo(zapatillas, xl, blanco, 25.00), a -> a.getProducto().getNombre().equals("Zapatillas Deportivas") && a.getTalla().getTalla().equals("XL") && a.getColor().getColor().equals("Blanco"));
            }

            // Insertar stock
            if (stockRepository.count() == 0) {
                Almacen tiendaCentral = almacenRepository.findByNombre("Tienda Central").orElseThrow();
                Almacen almacenNorte = almacenRepository.findByNombre("Almacén Norte").orElseThrow();

                Articulo camisetaS = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "S", "Rojo").orElseThrow();
                Articulo camisetaM = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "M", "Azul").orElseThrow();
                Articulo pantalonL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Pantalón Vaquero", "L", "Negro").orElseThrow();
                Articulo zapatillasXL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Zapatillas Deportivas", "XL", "Blanco").orElseThrow();

                saveIfNotExists(stockRepository, new Stock(camisetaS, tiendaCentral, 50), s -> s.getArticulo().equals(camisetaS) && s.getAlmacen().equals(tiendaCentral));
                saveIfNotExists(stockRepository, new Stock(camisetaM, tiendaCentral, 30), s -> s.getArticulo().equals(camisetaM) && s.getAlmacen().equals(tiendaCentral));
                saveIfNotExists(stockRepository, new Stock(pantalonL, almacenNorte, 20), s -> s.getArticulo().equals(pantalonL) && s.getAlmacen().equals(almacenNorte));
                saveIfNotExists(stockRepository, new Stock(zapatillasXL, tiendaCentral, 10), s -> s.getArticulo().equals(zapatillasXL) && s.getAlmacen().equals(tiendaCentral));
            }

            // Insertar ventas y detalles de ventas
            if (ventaRepository.count() == 0) {
                Cliente juan = clienteRepository.findByNombre("Juan Pérez").orElseThrow();
                Cliente maria = clienteRepository.findByNombre("María López").orElseThrow();
                Almacen tiendaCentral = almacenRepository.findByNombre("Tienda Central").orElseThrow();

                Venta ventaJuan = new Venta(juan, tiendaCentral, 39.98, 47.98, "ticket001", "completada", new Date());
                saveIfNotExists(ventaRepository, ventaJuan, v -> v.getNumeroTicket().equals("ticket001"));

                Venta ventaMaria = new Venta(maria, tiendaCentral, 49.99, 59.99, "ticket002", "completada", new Date());
                saveIfNotExists(ventaRepository, ventaMaria, v -> v.getNumeroTicket().equals("ticket002"));

                Articulo camisetaS = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "S", "Rojo").orElseThrow();
                Articulo pantalonL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Pantalón Vaquero", "L", "Negro").orElseThrow();
                Articulo zapatillasXL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Zapatillas Deportivas", "XL", "Blanco").orElseThrow();

                // Aquí calculamos precioSinIva, iva, y precioTotal
                double precioSinIvaCamiseta = 9.99 * 2;
                double ivaCamiseta = precioSinIvaCamiseta * 0.21;
                double precioTotalCamiseta = precioSinIvaCamiseta + ivaCamiseta;

                double precioSinIvaPantalon = 29.99 * 1;
                double ivaPantalon = precioSinIvaPantalon * 0.21;
                double precioTotalPantalon = precioSinIvaPantalon + ivaPantalon;

                double precioSinIvaZapatillas = 49.99 * 1;
                double ivaZapatillas = precioSinIvaZapatillas * 0.21;
                double precioTotalZapatillas = precioSinIvaZapatillas + ivaZapatillas;

                // Insertamos los detalles de venta usando los setters
                DetalleVenta detalleVentaJuanCamiseta = new DetalleVenta();
                detalleVentaJuanCamiseta.setVenta(ventaJuan);
                detalleVentaJuanCamiseta.setArticulo(camisetaS);
                detalleVentaJuanCamiseta.setCantidad(2);
                detalleVentaJuanCamiseta.setPrecioUnitario(9.99);
                detalleVentaJuanCamiseta.setPrecioSinIva(precioSinIvaCamiseta);
                detalleVentaJuanCamiseta.setIva(ivaCamiseta);
                detalleVentaJuanCamiseta.setPrecioTotal(precioTotalCamiseta);

                DetalleVenta detalleVentaJuanPantalon = new DetalleVenta();
                detalleVentaJuanPantalon.setVenta(ventaJuan);
                detalleVentaJuanPantalon.setArticulo(pantalonL);
                detalleVentaJuanPantalon.setCantidad(1);
                detalleVentaJuanPantalon.setPrecioUnitario(29.99);
                detalleVentaJuanPantalon.setPrecioSinIva(precioSinIvaPantalon);
                detalleVentaJuanPantalon.setIva(ivaPantalon);
                detalleVentaJuanPantalon.setPrecioTotal(precioTotalPantalon);

                DetalleVenta detalleVentaMariaZapatillas = new DetalleVenta();
                detalleVentaMariaZapatillas.setVenta(ventaMaria);
                detalleVentaMariaZapatillas.setArticulo(zapatillasXL);
                detalleVentaMariaZapatillas.setCantidad(1);
                detalleVentaMariaZapatillas.setPrecioUnitario(49.99);
                detalleVentaMariaZapatillas.setPrecioSinIva(precioSinIvaZapatillas);
                detalleVentaMariaZapatillas.setIva(ivaZapatillas);
                detalleVentaMariaZapatillas.setPrecioTotal(precioTotalZapatillas);

                // Guardar los detalles de venta
                detalleVentaRepository.save(detalleVentaJuanCamiseta);
                detalleVentaRepository.save(detalleVentaJuanPantalon);
                detalleVentaRepository.save(detalleVentaMariaZapatillas);
            }


            System.out.println("Carga de datos completada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Snippet para evitar registros duplicados
    private <T> void saveIfNotExists(JpaRepository<T, ?> repository, T entity, java.util.function.Predicate<T> predicate) {
        Optional<T> existingEntity = repository.findAll().stream().filter(predicate).findFirst();
        if (existingEntity.isEmpty()) {
            repository.save(entity);
        }
    }
}