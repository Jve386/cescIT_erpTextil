package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tallas")  // Nombre de la tabla en la base de datos
public class Talla {

    @Id
    private Long id;
    private String talla;

    // Constructor por defecto
    public Talla() {}

    // Constructor con parámetros
    public Talla(String talla) {
        this.talla = talla;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdService(Long id) {
        this.id = id;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
}
