package com.tiendatextil.model;

import javax.persistence.*;

@Entity
@Table(name = "almacenes")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlmacen;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_almacen", nullable = false)
    private TipoAlmacen tipoAlmacen;

    // Getters y setters
    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoAlmacen getTipoAlmacen() {
        return tipoAlmacen;
    }

    public void setTipoAlmacen(TipoAlmacen tipoAlmacen) {
        this.tipoAlmacen = tipoAlmacen;
    }
}
