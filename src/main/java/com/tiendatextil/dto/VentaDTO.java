package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VentaDTO {

    private String clienteNombre;
    private String productoNombre;
    private String talla;
    private String color;
    private int cantidad;
    private double precioUnitario;
    private double precioTotal;
    private Date fechaVenta;
    private String almacenNombre;

    public VentaDTO(String clienteNombre, String productoNombre, String talla, String color,
                    int cantidad, double precioUnitario, double precioTotal, Date fechaVenta, String almacenNombre) {
        this.clienteNombre = clienteNombre;
        this.productoNombre = productoNombre;
        this.talla = talla;
        this.color = color;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.fechaVenta = fechaVenta;
        this.almacenNombre = almacenNombre;
    }
}
