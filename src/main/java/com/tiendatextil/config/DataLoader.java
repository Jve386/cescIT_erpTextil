package com.tiendatextil.config;

import com.tiendatextil.model.*;
import com.tiendatextil.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

            // Insertar datos de ejemplo en la tabla de roles
            if (rolRepository.count() == 0) {
                System.out.println("Insertando roles...");
                rolRepository.save(new Rol("Administrador"));
                rolRepository.save(new Rol("Vendedor"));
            }

            // Insertar datos de ejemplo en la tabla de categorías
            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new Categoria("Camisetas"));
                categoriaRepository.save(new Categoria("Pantalones"));
                categoriaRepository.save(new Categoria("Zapatos"));
            }

            // Insertar datos de ejemplo en la tabla de tallas
            if (tallaRepository.count() == 0) {
                tallaRepository.save(new Talla("S"));
                tallaRepository.save(new Talla("M"));
                tallaRepository.save(new Talla("L"));
                tallaRepository.save(new Talla("XL"));
            }

            // Insertar datos de ejemplo en la tabla de colores
            if (colorRepository.count() == 0) {
                colorRepository.save(new Color("Rojo"));
                colorRepository.save(new Color("Azul"));
                colorRepository.save(new Color("Negro"));
                colorRepository.save(new Color("Blanco"));
            }

            // Insertar datos de ejemplo en la tabla de tipo de almacenes
            if (tipoAlmacenRepository.count() == 0) {
                tipoAlmacenRepository.save(new TipoAlmacen("Tienda"));
                tipoAlmacenRepository.save(new TipoAlmacen("Almacén"));
            }

            // Insertar almacenes
            if (almacenRepository.count() == 0) {
                TipoAlmacen tienda = tipoAlmacenRepository.findByTipo("Tienda").orElseThrow();
                TipoAlmacen almacen = tipoAlmacenRepository.findByTipo("Almacén").orElseThrow();

                almacenRepository.save(new Almacen("Tienda Central", "Calle Principal 123", tienda));
                almacenRepository.save(new Almacen("Almacén Norte", "Avenida Industrial 456", almacen));
            }

            // Insertar clientes
            if (clienteRepository.count() == 0) {
                clienteRepository.save(new Cliente("Juan Pérez", "juan@example.com", "123456789"));
                clienteRepository.save(new Cliente("María López", "maria@example.com", "987654321"));
            }

            // Insertar productos
            if (productoRepository.count() == 0) {
                Categoria camisetas = categoriaRepository.findByNombre("Camisetas").orElseThrow();
                Categoria pantalones = categoriaRepository.findByNombre("Pantalones").orElseThrow();
                Categoria zapatos = categoriaRepository.findByNombre("Zapatos").orElseThrow();

                productoRepository.save(new Producto("Camiseta Básica", "Camiseta de algodón", 9.99, camisetas));
                productoRepository.save(new Producto("Pantalón Vaquero", "Pantalón de mezclilla", 29.99, pantalones));
                productoRepository.save(new Producto("Zapatillas Deportivas", "Zapatillas para correr", 49.99, zapatos));
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

                articuloRepository.save(new Articulo(camiseta, s, rojo, 5.00));
                articuloRepository.save(new Articulo(camiseta, m, azul, 5.50));
                articuloRepository.save(new Articulo(pantalon, l, negro, 15.00));
                articuloRepository.save(new Articulo(zapatillas, xl, blanco, 25.00));
            }

            // Insertar stock
            if (stockRepository.count() == 0) {
                Almacen tiendaCentral = almacenRepository.findByNombre("Tienda Central").orElseThrow();
                Almacen almacenNorte = almacenRepository.findByNombre("Almacén Norte").orElseThrow();

                Articulo camisetaS = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "S", "Rojo").orElseThrow();
                Articulo camisetaM = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "M", "Azul").orElseThrow();
                Articulo pantalonL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Pantalón Vaquero", "L", "Negro").orElseThrow();
                Articulo zapatillasXL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Zapatillas Deportivas", "XL", "Blanco").orElseThrow();

                stockRepository.save(new Stock(camisetaS, tiendaCentral, 50));
                stockRepository.save(new Stock(camisetaM, tiendaCentral, 30));
                stockRepository.save(new Stock(pantalonL, almacenNorte, 20));
                stockRepository.save(new Stock(zapatillasXL, tiendaCentral, 10));
            }

            // Insertar ventas y detalles de ventas
            if (ventaRepository.count() == 0) {
                Cliente juan = clienteRepository.findByNombre("Juan Pérez").orElseThrow();
                Cliente maria = clienteRepository.findByNombre("María López").orElseThrow();
                Almacen tiendaCentral = almacenRepository.findByNombre("Tienda Central").orElseThrow();

                Venta ventaJuan = new Venta(juan, tiendaCentral, 39.98, 47.98, "ticket001", "completada");
                ventaRepository.save(ventaJuan);

                Venta ventaMaria = new Venta(maria, tiendaCentral, 49.99, 59.99, "ticket002", "completada");
                ventaRepository.save(ventaMaria);

                Articulo camisetaS = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Camiseta Básica", "S", "Rojo").orElseThrow();
                Articulo pantalonL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Pantalón Vaquero", "L", "Negro").orElseThrow();
                Articulo zapatillasXL = articuloRepository.findByProductoNombreAndTallaTallaAndColorColor("Zapatillas Deportivas", "XL", "Blanco").orElseThrow();

                detalleVentaRepository.save(new DetalleVenta(ventaJuan, camisetaS, 2, 9.99, 19.98, 8.00, 23.98));
                detalleVentaRepository.save(new DetalleVenta(ventaJuan, pantalonL, 1, 29.99, 29.99, 8.00, 35.98));
                detalleVentaRepository.save(new DetalleVenta(ventaMaria, zapatillasXL, 1, 49.99, 49.99, 10.00, 59.99));
            }
            System.out.println("Carga de datos completada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
            throw e; // Para asegurarte de que Spring vea la excepción
        }
    }
}
