package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticuloDTO {

    private Long id;
    private Long idProducto;
    private Long idTalla;
    private Long idColor;
    private double precio;

    public ArticuloDTO() {}

    public ArticuloDTO(Long id, Long idProducto, Long idTalla, Long idColor, double precio) {
        this.id = id;
        this.idProducto = idProducto;
        this.idTalla = idTalla;
        this.idColor = idColor;
        this.precio = precio;
    }
}
