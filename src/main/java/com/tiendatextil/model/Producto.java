package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productos")  // Nombre de la tabla en la base de datos
public class Producto {

    @Id
    private Long id;
    private String nombre;
    private String descripcion;
    private double precioBase;

    @ManyToOne
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

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdProducto(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
