package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "almacen_id")
    private Almacen almacen;

    private int cantidad;

    public Stock(Articulo articulo, Almacen almacen, int cantidad) {
        this.articulo = articulo;
        this.almacen = almacen;
        this.cantidad = cantidad;
    }
}
