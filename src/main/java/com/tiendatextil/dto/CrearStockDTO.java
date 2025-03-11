package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearStockDTO {
    private Long idArticulo;
    private Long idAlmacen;
    private int cantidad;

    // Constructor vacío necesario para Jackson
    public CrearStockDTO() {
    }

}