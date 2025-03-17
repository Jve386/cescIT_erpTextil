package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticuloDTO {
    private Long id;
    private String nombreProducto;
    private String talla;
    private String color;
    private Double precioCoste;
    private Double precioVenta;

    // Constructor vacío necesario para Jackson
    public ArticuloDTO() {
    }    
}
