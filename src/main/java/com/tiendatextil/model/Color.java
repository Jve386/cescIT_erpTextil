package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "colores")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;

    @Column(nullable = false, unique = true)
    private String color;

    // Getters y setters
    public Long getIdColor() {
        return idColor;
    }

    public void setIdColor(Long idColor) {
        this.idColor = idColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
