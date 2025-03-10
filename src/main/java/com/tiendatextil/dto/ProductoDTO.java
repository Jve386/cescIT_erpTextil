package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private Long idCategoria;

    public ProductoDTO() {}

    public ProductoDTO(Long id, String nombre, String descripcion, double precioBase, Long idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.idCategoria = idCategoria;
    }
}
