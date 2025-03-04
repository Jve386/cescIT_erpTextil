package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Rol {

    @Id
    private Long idRol;
    private String nombre;

    // Constructor
    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
