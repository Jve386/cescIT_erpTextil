package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_articulo", nullable = false)
    private Articulo articulo;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;

    @Column(name = "precio_sin_iva", nullable = false)
    private double precioSinIva;

    @Column(nullable = false)
    private double iva;

    @Column(name = "precio_total", nullable = false)
    private double precioTotal;

    // Constructor por defecto
    public DetalleVenta() {}

    // Constructor con parámetros
    public DetalleVenta(Venta venta, Articulo articulo, int cantidad, double precioUnitario, double precioSinIva, double iva, double precioTotal) {
        this.venta = venta;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioSinIva = precioSinIva;
        this.iva = iva;
        this.precioTotal = precioTotal;
    }
}
