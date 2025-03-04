package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    @ManyToOne
    @JoinColumn(name = "id_articulo", nullable = false)
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @Column(nullable = false)
    private Integer cantidad;

    // Getters y setters
    public Long getIdStock() {
        return idStock;
    }

    public void setIdStock(Long idStock) {
        this.idStock = idStock;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
