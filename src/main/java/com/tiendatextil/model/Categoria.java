package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "categorias")
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;  // Este campo se generará automáticamente

    @Column(nullable = false, unique = true)
    private String nombre;

    public Categoria() {}


    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
