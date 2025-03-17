package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_talla", nullable = false)
    private Talla talla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_color", nullable = false)
    private Color color;

    @Column(name = "precio_coste")
    private Double precioCoste;
    
    @Column(name = "precio_venta")
    private Double precioVenta;

    public Articulo() {}

    public Articulo(Producto producto, Talla talla, Color color, Double precioCoste, Double precioVenta) {
        this.producto = producto;
        this.talla = talla;
        this.color = color;
        this.precioCoste = precioCoste;
        this.precioVenta = precioVenta;
    }
}
