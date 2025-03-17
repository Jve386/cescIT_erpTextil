package com.tiendatextil.config;

import com.tiendatextil.model.*;
import com.tiendatextil.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;


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
                saveIfNotExists(rolRepository, new Rol("Gerente"), r -> r.getNombre().equals("Gerente"));
            }

            // Insertar datos de ejemplo en la tabla de categorías
            if (categoriaRepository.count() == 0) {
                saveIfNotExists(categoriaRepository, new Categoria("Camisetas"), c -> c.getNombre().equals("Camisetas"));
                saveIfNotExists(categoriaRepository, new Categoria("Pantalones"), c -> c.getNombre().equals("Pantalones"));
                saveIfNotExists(categoriaRepository, new Categoria("Zapatos"), c -> c.getNombre().equals("Zapatos"));
                saveIfNotExists(categoriaRepository, new Categoria("Vestidos"), c -> c.getNombre().equals("Vestidos"));
                saveIfNotExists(categoriaRepository, new Categoria("Chaquetas"), c -> c.getNombre().equals("Chaquetas"));
                saveIfNotExists(categoriaRepository, new Categoria("Accesorios"), c -> c.getNombre().equals("Accesorios"));
            }

            // Insertar datos de ejemplo en la tabla de tallas
            if (tallaRepository.count() == 0) {
                saveIfNotExists(tallaRepository, new Talla("XS"), t -> t.getTalla().equals("XS"));
                saveIfNotExists(tallaRepository, new Talla("S"), t -> t.getTalla().equals("S"));
                saveIfNotExists(tallaRepository, new Talla("M"), t -> t.getTalla().equals("M"));
                saveIfNotExists(tallaRepository, new Talla("L"), t -> t.getTalla().equals("L"));
                saveIfNotExists(tallaRepository, new Talla("XL"), t -> t.getTalla().equals("XL"));
                saveIfNotExists(tallaRepository, new Talla("XXL"), t -> t.getTalla().equals("XXL"));
                saveIfNotExists(tallaRepository, new Talla("36"), t -> t.getTalla().equals("36"));
                saveIfNotExists(tallaRepository, new Talla("38"), t -> t.getTalla().equals("38"));
                saveIfNotExists(tallaRepository, new Talla("40"), t -> t.getTalla().equals("40"));
                saveIfNotExists(tallaRepository, new Talla("42"), t -> t.getTalla().equals("42"));
            }

            // Insertar datos de ejemplo en la tabla de colores
            if (colorRepository.count() == 0) {
                saveIfNotExists(colorRepository, new Color("Rojo"), c -> c.getColor().equals("Rojo"));
                saveIfNotExists(colorRepository, new Color("Azul"), c -> c.getColor().equals("Azul"));
                saveIfNotExists(colorRepository, new Color("Negro"), c -> c.getColor().equals("Negro"));
                saveIfNotExists(colorRepository, new Color("Blanco"), c -> c.getColor().equals("Blanco"));
                saveIfNotExists(colorRepository, new Color("Verde"), c -> c.getColor().equals("Verde"));
                saveIfNotExists(colorRepository, new Color("Amarillo"), c -> c.getColor().equals("Amarillo"));
                saveIfNotExists(colorRepository, new Color("Rosa"), c -> c.getColor().equals("Rosa"));
                saveIfNotExists(colorRepository, new Color("Gris"), c -> c.getColor().equals("Gris"));
                saveIfNotExists(colorRepository, new Color("Morado"), c -> c.getColor().equals("Morado"));
                saveIfNotExists(colorRepository, new Color("Marrón"), c -> c.getColor().equals("Marrón"));
            }

            // Insertar almacenes
            if (almacenRepository.count() == 0) {
                saveIfNotExists(almacenRepository,
                        new Almacen("Tienda Central", "Calle Principal 123", TipoAlmacen.TIENDA),
                        a -> a.getNombre().equals("Tienda Central"));

                saveIfNotExists(almacenRepository,
                        new Almacen("Almacén Norte", "Avenida Industrial 456", TipoAlmacen.ALMACEN),
                        a -> a.getNombre().equals("Almacén Norte"));
                
                saveIfNotExists(almacenRepository,
                        new Almacen("Tienda Sur", "Avenida del Sur 789", TipoAlmacen.TIENDA),
                        a -> a.getNombre().equals("Tienda Sur"));
                
                saveIfNotExists(almacenRepository,
                        new Almacen("Almacén Central", "Polígono Industrial 101", TipoAlmacen.ALMACEN),
                        a -> a.getNombre().equals("Almacén Central"));
            }

            // Insertar clientes
            if (clienteRepository.count() == 0) {
                saveIfNotExists(clienteRepository, new Cliente("Juan Pérez", "juan@example.com", "123456789"), c -> c.getNombre().equals("Juan Pérez"));
                saveIfNotExists(clienteRepository, new Cliente("María López", "maria@example.com", "987654321"), c -> c.getNombre().equals("María López"));
                saveIfNotExists(clienteRepository, new Cliente("Carlos Rodríguez", "carlos@example.com", "555123456"), c -> c.getNombre().equals("Carlos Rodríguez"));
                saveIfNotExists(clienteRepository, new Cliente("Ana Martínez", "ana@example.com", "555789012"), c -> c.getNombre().equals("Ana Martínez"));
                saveIfNotExists(clienteRepository, new Cliente("Pedro Sánchez", "pedro@example.com", "555345678"), c -> c.getNombre().equals("Pedro Sánchez"));
            }

            // Insertar productos (sin precio_base)
            if (productoRepository.count() == 0) {
                Categoria camisetas = categoriaRepository.findByNombre("Camisetas").orElseThrow();
                Categoria pantalones = categoriaRepository.findByNombre("Pantalones").orElseThrow();
                Categoria zapatos = categoriaRepository.findByNombre("Zapatos").orElseThrow();
                Categoria vestidos = categoriaRepository.findByNombre("Vestidos").orElseThrow();
                Categoria chaquetas = categoriaRepository.findByNombre("Chaquetas").orElseThrow();
                Categoria accesorios = categoriaRepository.findByNombre("Accesorios").orElseThrow();

                saveIfNotExists(productoRepository, 
                    new Producto("Camiseta Básica", "Camiseta de algodón", camisetas), 
                    p -> p.getNombre().equals("Camiseta Básica"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Camiseta Estampada", "Camiseta con diseño gráfico", camisetas), 
                    p -> p.getNombre().equals("Camiseta Estampada"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Pantalón Vaquero", "Pantalón de mezclilla", pantalones), 
                    p -> p.getNombre().equals("Pantalón Vaquero"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Pantalón Chino", "Pantalón casual de algodón", pantalones), 
                    p -> p.getNombre().equals("Pantalón Chino"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Zapatillas Deportivas", "Zapatillas para correr", zapatos), 
                    p -> p.getNombre().equals("Zapatillas Deportivas"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Zapatos Formales", "Zapatos de vestir", zapatos), 
                    p -> p.getNombre().equals("Zapatos Formales"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Vestido Casual", "Vestido para uso diario", vestidos), 
                    p -> p.getNombre().equals("Vestido Casual"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Vestido de Fiesta", "Vestido elegante para eventos", vestidos), 
                    p -> p.getNombre().equals("Vestido de Fiesta"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Chaqueta Vaquera", "Chaqueta de mezclilla", chaquetas), 
                    p -> p.getNombre().equals("Chaqueta Vaquera"));
                
                saveIfNotExists(productoRepository, 
                    new Producto("Bufanda", "Bufanda de lana", accesorios), 
                    p -> p.getNombre().equals("Bufanda"));
            }

            // Insertar artículos (combinación de producto, talla y color) con precio_coste y precio_venta
            if (articuloRepository.count() == 0) {
                // Productos
                Producto camisetaBasica = productoRepository.findByNombre("Camiseta Básica").orElseThrow();
                Producto camisetaEstampada = productoRepository.findByNombre("Camiseta Estampada").orElseThrow();
                Producto pantalonVaquero = productoRepository.findByNombre("Pantalón Vaquero").orElseThrow();
                Producto pantalonChino = productoRepository.findByNombre("Pantalón Chino").orElseThrow();
                Producto zapatillasDeportivas = productoRepository.findByNombre("Zapatillas Deportivas").orElseThrow();
                Producto zapatosFormales = productoRepository.findByNombre("Zapatos Formales").orElseThrow();
                Producto vestidoCasual = productoRepository.findByNombre("Vestido Casual").orElseThrow();
                Producto vestidoFiesta = productoRepository.findByNombre("Vestido de Fiesta").orElseThrow();
                Producto chaquetaVaquera = productoRepository.findByNombre("Chaqueta Vaquera").orElseThrow();
                Producto bufanda = productoRepository.findByNombre("Bufanda").orElseThrow();

                // Tallas
                Talla xs = tallaRepository.findByTalla("XS").orElseThrow();
                Talla s = tallaRepository.findByTalla("S").orElseThrow();
                Talla m = tallaRepository.findByTalla("M").orElseThrow();
                Talla l = tallaRepository.findByTalla("L").orElseThrow();
                Talla xl = tallaRepository.findByTalla("XL").orElseThrow();
                Talla xxl = tallaRepository.findByTalla("XXL").orElseThrow();
                Talla t36 = tallaRepository.findByTalla("36").orElseThrow();
                Talla t38 = tallaRepository.findByTalla("38").orElseThrow();
                Talla t40 = tallaRepository.findByTalla("40").orElseThrow();
                Talla t42 = tallaRepository.findByTalla("42").orElseThrow();

                // Colores
                Color rojo = colorRepository.findByColor("Rojo").orElseThrow();
                Color azul = colorRepository.findByColor("Azul").orElseThrow();
                Color negro = colorRepository.findByColor("Negro").orElseThrow();
                Color blanco = colorRepository.findByColor("Blanco").orElseThrow();
                Color verde = colorRepository.findByColor("Verde").orElseThrow();
                Color amarillo = colorRepository.findByColor("Amarillo").orElseThrow();
                Color rosa = colorRepository.findByColor("Rosa").orElseThrow();
                Color gris = colorRepository.findByColor("Gris").orElseThrow();
                Color morado = colorRepository.findByColor("Morado").orElseThrow();
                Color marron = colorRepository.findByColor("Marrón").orElseThrow();

                // Crear artículos con precio_coste y precio_venta (50% markup)
                // Camisetas básicas
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaBasica, s, rojo, 5.00, 7.50), 
                    a -> a.getProducto().getNombre().equals("Camiseta Básica") && 
                         a.getTalla().getTalla().equals("S") && 
                         a.getColor().getColor().equals("Rojo"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaBasica, m, azul, 5.50, 8.25), 
                    a -> a.getProducto().getNombre().equals("Camiseta Básica") && 
                         a.getTalla().getTalla().equals("M") && 
                         a.getColor().getColor().equals("Azul"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaBasica, l, negro, 5.50, 8.25), 
                    a -> a.getProducto().getNombre().equals("Camiseta Básica") && 
                         a.getTalla().getTalla().equals("L") && 
                         a.getColor().getColor().equals("Negro"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaBasica, xl, blanco, 6.00, 9.00), 
                    a -> a.getProducto().getNombre().equals("Camiseta Básica") && 
                         a.getTalla().getTalla().equals("XL") && 
                         a.getColor().getColor().equals("Blanco"));
                
                // Camisetas estampadas
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaEstampada, s, verde, 7.00, 10.50), 
                    a -> a.getProducto().getNombre().equals("Camiseta Estampada") && 
                         a.getTalla().getTalla().equals("S") && 
                         a.getColor().getColor().equals("Verde"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(camisetaEstampada, m, amarillo, 7.50, 11.25), 
                    a -> a.getProducto().getNombre().equals("Camiseta Estampada") && 
                         a.getTalla().getTalla().equals("M") && 
                         a.getColor().getColor().equals("Amarillo"));
                
                // Pantalones vaqueros
                saveIfNotExists(articuloRepository, 
                    new Articulo(pantalonVaquero, m, azul, 15.00, 22.50), 
                    a -> a.getProducto().getNombre().equals("Pantalón Vaquero") && 
                         a.getTalla().getTalla().equals("M") && 
                         a.getColor().getColor().equals("Azul"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(pantalonVaquero, l, negro, 15.00, 22.50), 
                    a -> a.getProducto().getNombre().equals("Pantalón Vaquero") && 
                         a.getTalla().getTalla().equals("L") && 
                         a.getColor().getColor().equals("Negro"));
                
                // Pantalones chinos
                saveIfNotExists(articuloRepository, 
                    new Articulo(pantalonChino, m, gris, 18.00, 27.00), 
                    a -> a.getProducto().getNombre().equals("Pantalón Chino") && 
                         a.getTalla().getTalla().equals("M") && 
                         a.getColor().getColor().equals("Gris"));
                
                // Zapatillas deportivas
                saveIfNotExists(articuloRepository, 
                    new Articulo(zapatillasDeportivas, t38, blanco, 25.00, 37.50), 
                    a -> a.getProducto().getNombre().equals("Zapatillas Deportivas") && 
                         a.getTalla().getTalla().equals("38") && 
                         a.getColor().getColor().equals("Blanco"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(zapatillasDeportivas, t40, negro, 25.00, 37.50), 
                    a -> a.getProducto().getNombre().equals("Zapatillas Deportivas") && 
                         a.getTalla().getTalla().equals("40") && 
                         a.getColor().getColor().equals("Negro"));
                
                // Zapatos formales
                saveIfNotExists(articuloRepository, 
                    new Articulo(zapatosFormales, t42, marron, 35.00, 52.50), 
                    a -> a.getProducto().getNombre().equals("Zapatos Formales") && 
                         a.getTalla().getTalla().equals("42") && 
                         a.getColor().getColor().equals("Marrón"));
                
                // Vestidos
                saveIfNotExists(articuloRepository, 
                    new Articulo(vestidoCasual, s, rosa, 20.00, 30.00), 
                    a -> a.getProducto().getNombre().equals("Vestido Casual") && 
                         a.getTalla().getTalla().equals("S") && 
                         a.getColor().getColor().equals("Rosa"));
                
                saveIfNotExists(articuloRepository, 
                    new Articulo(vestidoFiesta, m, rojo, 45.00, 67.50), 
                    a -> a.getProducto().getNombre().equals("Vestido de Fiesta") && 
                         a.getTalla().getTalla().equals("M") && 
                         a.getColor().getColor().equals("Rojo"));
                
                // Chaquetas
                saveIfNotExists(articuloRepository, 
                    new Articulo(chaquetaVaquera, l, azul, 30.00, 45.00), 
                    a -> a.getProducto().getNombre().equals("Chaqueta Vaquera") && 
                         a.getTalla().getTalla().equals("L") && 
                         a.getColor().getColor().equals("Azul"));
                
                // Accesorios
                saveIfNotExists(articuloRepository, 
                    new Articulo(bufanda, s, gris, 8.00, 12.00), 
                    a -> a.getProducto().getNombre().equals("Bufanda") && 
                         a.getTalla().getTalla().equals("S") && 
                         a.getColor().getColor().equals("Gris"));
            }

            // Insertar stock
            if (stockRepository.count() == 0) {
                Almacen tiendaCentral = almacenRepository.findByNombre("Tienda Central").orElseThrow();
                Almacen almacenNorte = almacenRepository.findByNombre("Almacén Norte").orElseThrow();
                Almacen tiendaSur = almacenRepository.findByNombre("Tienda Sur").orElseThrow();
                Almacen almacenCentral = almacenRepository.findByNombre("Almacén Central").orElseThrow();

                // Obtener todos los artículos
                List<Articulo> articulos = articuloRepository.findAll();
                
                // Distribuir stock entre los almacenes
                for (Articulo articulo : articulos) {
                    // Tienda Central - todos los artículos con stock variado
                    saveIfNotExists(stockRepository, 
                        new Stock(articulo, tiendaCentral, (int)(Math.random() * 50) + 10), 
                        s -> s.getArticulo().equals(articulo) && s.getAlmacen().equals(tiendaCentral));
                    
                    // Almacén Norte - solo algunos artículos con stock alto
                    if (Math.random() > 0.3) {
                        saveIfNotExists(stockRepository, 
                            new Stock(articulo, almacenNorte, (int)(Math.random() * 100) + 20), 
                            s -> s.getArticulo().equals(articulo) && s.getAlmacen().equals(almacenNorte));
                    }
                    
                    // Tienda Sur - pocos artículos con stock bajo
                    if (Math.random() > 0.7) {
                        saveIfNotExists(stockRepository, 
                            new Stock(articulo, tiendaSur, (int)(Math.random() * 20) + 5), 
                            s -> s.getArticulo().equals(articulo) && s.getAlmacen().equals(tiendaSur));
                    }
                    
                    // Almacén Central - stock muy alto para algunos artículos
                    if (Math.random() > 0.5) {
                        saveIfNotExists(stockRepository, 
                            new Stock(articulo, almacenCentral, (int)(Math.random() * 200) + 50), 
                            s -> s.getArticulo().equals(articulo) && s.getAlmacen().equals(almacenCentral));
                    }
                }
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
