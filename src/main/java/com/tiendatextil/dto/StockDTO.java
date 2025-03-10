package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
    private Long id;
    private Long idArticulo;
    private Long idAlmacen;
    private int cantidad;

    public StockDTO() {}

    public StockDTO(Long id, Long idArticulo, Long idAlmacen, int cantidad) {
        this.id = id;
        this.idArticulo = idArticulo;
        this.idAlmacen = idAlmacen;
        this.cantidad = cantidad;
    }
}
