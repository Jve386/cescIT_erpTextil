package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_articulo", "id_almacen"}))
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    private int cantidad;

    public Stock() {
    }

    public Stock(Articulo articulo, Almacen almacen, int cantidad) {
        this.articulo = articulo;
        this.almacen = almacen;
        this.cantidad = cantidad;
    }
}
