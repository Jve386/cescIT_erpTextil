package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaDTO {
    private Long idArticulo;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    private Double precioUnitario;
    private Double precioTotal;
}
