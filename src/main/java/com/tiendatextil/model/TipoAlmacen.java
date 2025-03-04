package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tipo_almacen")
public class TipoAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_almacen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String tipo;

    public TipoAlmacen() {
    }

    public TipoAlmacen(String tipo) {
        this.tipo = tipo;
    }
}
