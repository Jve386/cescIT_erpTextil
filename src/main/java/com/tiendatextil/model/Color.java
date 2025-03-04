package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "colores")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Se agrega esta anotación para generar el ID automáticamente
    @Column(name = "id_color")
    private Long id;

    @Column(nullable = false, unique = true)
    private String color;


    public Color() {}

    public Color(String color) {
        this.color = color;
    }
}
