package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaDTO {
    private Long id;
    private Long idVenta;
    private Long idArticulo;
    private int cantidad;
    private double precioUnitario;
    private double precioSinIva;
    private double iva;
    private double precioTotal;

    public DetalleVentaDTO() {}

    public DetalleVentaDTO(Long id, Long idVenta, Long idArticulo, int cantidad, double precioUnitario, double precioSinIva, double iva, double precioTotal) {
        this.id = id;
        this.idVenta = idVenta;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioSinIva = precioSinIva;
        this.iva = iva;
        this.precioTotal = precioTotal;
    }
}
