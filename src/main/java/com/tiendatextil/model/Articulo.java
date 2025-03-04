package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    private Long id;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Talla talla;

    @ManyToOne
    private Color color;

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

    // Getters y setters
    public Long getIdArticulo() {
        return id;
    }

    public void setIdArticulo(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Talla getTalla() {
        return talla;
    }

    public void setTalla(Talla talla) {
        this.talla = talla;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
