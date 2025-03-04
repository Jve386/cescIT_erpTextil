package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;  // Este campo se generará automáticamente

    @Column(nullable = false, unique = true)
    private String nombre;

    // Constructor por defecto
    public Rol() {}

    // Constructor con parámetros
    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
