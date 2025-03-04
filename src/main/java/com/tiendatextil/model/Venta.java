package com.tiendatextil.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha = new Date();

    @Column(nullable = false)
    private Double totalSinIva;

    @Column(nullable = false)
    private Double totalConIva;

    @Column(nullable = false, unique = true)
    private String numeroTicket;

    @Column(nullable = false)
    private String estado = "completada";

    // Getters y setters
    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotalSinIva() {
        return totalSinIva;
    }

    public void setTotalSinIva(Double totalSinIva) {
        this.totalSinIva = totalSinIva;
    }

    public Double getTotalConIva() {
        return totalConIva;
    }

    public void setTotalConIva(Double totalConIva) {
        this.totalConIva = totalConIva;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
