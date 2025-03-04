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

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_talla", nullable = false)
    private Talla talla;

    @ManyToOne
    @JoinColumn(name = "id_color", nullable = false)
    private Color color;

    @Column(name = "precio_coste")
    private double precio;

    // Constructor por defecto
    public Articulo() {}

    // Constructor con parámetros
    public Articulo(Producto producto, Talla talla, Color color, double precio) {
        this.producto = producto;
        this.talla = talla;
        this.color = color;
        this.precio = precio;
    }
}
