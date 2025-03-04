package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tallas")
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_talla")
    private Long id;

    @Column(nullable = false, unique = true)
    private String talla;

    // Constructor por defecto
    public Talla() {}

    // Constructor con parámetros
    public Talla(String talla) {
        this.talla = talla;
    }
}
