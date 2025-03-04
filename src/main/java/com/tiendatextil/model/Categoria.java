package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categorias")  // Nombre de la tabla en la base de datos
public class Categoria {

    @Id
    private Long id;
    private String nombre;

    // Constructor por defecto
    public Categoria() {}

    // Constructor con parámetros
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setIdCategoria(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
