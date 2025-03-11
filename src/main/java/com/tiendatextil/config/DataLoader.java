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

            // Insertar almacenes
            if (almacenRepository.count() == 0) {
                saveIfNotExists(almacenRepository,
                        new Almacen("Tienda Central", "Calle Principal 123", TipoAlmacen.TIENDA),
                        a -> a.getNombre().equals("Tienda Central"));

                saveIfNotExists(almacenRepository,
                        new Almacen("Almacén Norte", "Avenida Industrial 456", TipoAlmacen.ALMACEN),
                        a -> a.getNombre().equals("Almacén Norte"));
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

            System.out.println("Carga de datos completada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar los datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private <T> void saveIfNotExists(JpaRepository<T, Long> repository, T entity, java.util.function.Predicate<T> condition) {
        if (repository.findAll().stream().noneMatch(condition)) {
            repository.save(entity);
        }
    }
}
