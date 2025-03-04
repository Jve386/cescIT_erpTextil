package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_almacen")  // Nombre de la tabla en la base de datos
public class TipoAlmacen {

    @Id
    private Long id;
    private String tipo;

    // Constructor por defecto
    public TipoAlmacen() {}

    // Constructor con parámetros
    public TipoAlmacen(String tipo) {
        this.tipo = tipo;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdAlmacen(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
