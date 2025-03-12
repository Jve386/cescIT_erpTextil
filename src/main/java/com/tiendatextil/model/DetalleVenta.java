package com.tiendatextil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "detalle_venta")
@Getter
@Setter
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long id;

    // In DetalleVenta.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonBackReference("venta-detalles")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_articulo", nullable = false)
    @JsonBackReference("articulo-detalles")
    private Articulo articulo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "precio_sin_iva", nullable = false)
    private Double precioSinIva;

    @Column(nullable = false)
    private Double iva;

    @Column(name = "precio_total", nullable = false)
    private Double precioTotal;

    @PrePersist
    protected void onPersist() {
        if (precioSinIva == null) {
            precioSinIva = cantidad * precioUnitario;
        }
        if (iva == null) {
            iva = precioSinIva * 0.21;
        }
        if (precioTotal == null) {
            precioTotal = precioSinIva + iva;
        }
    }

    public DetalleVenta() {
    }

    public DetalleVenta(Venta venta, Articulo articulo, int cantidad, double precioUnitario, double precioSinIva,
            double iva, double precioTotal) {
        this.venta = venta;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioSinIva = precioSinIva;
        this.iva = iva;
        this.precioTotal = precioTotal;
    }
}
