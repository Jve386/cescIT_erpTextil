package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "tipo_almacen")
public class TipoAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoAlmacen;

    @Column(nullable = false, unique = true)
    private String tipo;

    // Getters y setters
    public Long getIdTipoAlmacen() {
        return idTipoAlmacen;
    }

    public void setIdTipoAlmacen(Long idTipoAlmacen) {
        this.idTipoAlmacen = idTipoAlmacen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
