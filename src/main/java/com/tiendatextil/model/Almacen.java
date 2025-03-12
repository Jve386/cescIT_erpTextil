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

    @Enumerated(EnumType.STRING)  // ¡Asegura que esto esté presente!
    @Column(nullable = false, name = "tipo_almacen")
    private TipoAlmacen tipoAlmacen;

    public Almacen() {}

    public Almacen(String nombre, String direccion, TipoAlmacen tipoAlmacen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipoAlmacen = tipoAlmacen;
    }
}
