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

    @ManyToOne
    @JoinColumn(name = "id_tipo_almacen", nullable = false)
    private TipoAlmacen tipoAlmacen;


    public Almacen() {}


    public Almacen(String nombre, String direccion, TipoAlmacen tipoAlmacen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipoAlmacen = tipoAlmacen;
    }
}
