package com.tiendatextil.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @Column(name = "total_sin_iva", nullable = false)
    private double totalSinIva;

    @Column(name = "total_con_iva", nullable = false)
    private double totalConIva;

    @Column(name = "numero_ticket", unique = true, nullable = false)
    private String numeroTicket;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Venta() {}

    public Venta(Cliente cliente, Almacen almacen, double totalSinIva, double totalConIva, String numeroTicket, String estado, Date fecha) {
        this.cliente = cliente;
        this.almacen = almacen;
        this.totalSinIva = totalSinIva;
        this.totalConIva = totalConIva;
        this.numeroTicket = numeroTicket;
        this.estado = estado;
        this.fecha = fecha;
    }
}
