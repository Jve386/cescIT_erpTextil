package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "precio_base", nullable = false)
    private double precioBase;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Constructor por defecto
    public Producto() {}

    // Constructor con parámetros
    public Producto(String nombre, String descripcion, double precioBase, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.categoria = categoria;
    }
}
