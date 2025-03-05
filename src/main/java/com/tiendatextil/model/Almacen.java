package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "almacenes")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    // Ahora tipoAlmacen es simplemente un String, no es una relación con otra entidad.
    @Column(nullable = false)
    private String tipoAlmacen;

    // Constructor sin parámetros
    public Almacen() {}

    // Constructor con parámetros
    public Almacen(String nombre, String direccion, String tipoAlmacen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipoAlmacen = tipoAlmacen;
    }
}
