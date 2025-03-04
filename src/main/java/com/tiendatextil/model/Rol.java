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
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;


    public Rol() {}


    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
