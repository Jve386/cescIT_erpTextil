package com.tiendatextil.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DetalleVenta {

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    private int cantidad;
    private double precioUnitario;
    private double totalArticulo;
    private double descuento;
    private double totalConDescuento;

    // Constructor adecuado
    public DetalleVenta(Venta venta, Articulo articulo, int cantidad, double precioUnitario, double totalArticulo, double descuento, double totalConDescuento) {
        this.venta = venta;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.totalArticulo = totalArticulo;
        this.descuento = descuento;
        this.totalConDescuento = totalConDescuento;
    }

    // Getters y setters
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getTotalArticulo() {
        return totalArticulo;
    }

    public void setTotalArticulo(double totalArticulo) {
        this.totalArticulo = totalArticulo;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotalConDescuento() {
        return totalConDescuento;
    }

    public void setTotalConDescuento(double totalConDescuento) {
        this.totalConDescuento = totalConDescuento;
    }
}
