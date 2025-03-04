package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticulo;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_talla", nullable = false)
    private Talla talla;

    @ManyToOne
    @JoinColumn(name = "id_color", nullable = false)
    private Color color;

    @Column
    private Double precioCoste;

    // Getters y setters
    public Long getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Long idArticulo) {
        this.idArticulo = idArticulo;
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

    public Double getPrecioCoste() {
        return precioCoste;
    }

    public void setPrecioCoste(Double precioCoste) {
        this.precioCoste = precioCoste;
    }
}
