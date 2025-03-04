package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "tallas")
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTalla;

    @Column(nullable = false, unique = true)
    private String talla;

    // Getters y setters
    public Long getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Long idTalla) {
        this.idTalla = idTalla;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
}
