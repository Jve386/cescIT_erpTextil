package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "colores")  // Nombre de la tabla en la base de datos
public class Color {

    @Id
    private Long id;
    private String color;

    // Constructor por defecto
    public Color() {}

    // Constructor con parámetros
    public Color(String color) {
        this.color = color;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdColor(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
