package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "almacenes")  // Nombre de la tabla en la base de datos
public class Almacen {

    @Id
    private Long id;
    private String nombre;
    private String direccion;

    @ManyToOne
    private TipoAlmacen tipoAlmacen;

    // Constructor por defecto
    public Almacen() {}

    // Constructor con parámetros
    public Almacen(String nombre, String direccion, TipoAlmacen tipoAlmacen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipoAlmacen = tipoAlmacen;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdAlmacen(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoAlmacen getTipoAlmacen() {
        return tipoAlmacen;
    }

    public void setTipoAlmacen(TipoAlmacen tipoAlmacen) {
        this.tipoAlmacen = tipoAlmacen;
    }
}
