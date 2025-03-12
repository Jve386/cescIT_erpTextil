package com.tiendatextil.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @Column(nullable = false)
    private Date fecha;

    @Column(name = "total_sin_iva", nullable = false)
    private Double totalSinIva;

    @Column(name = "total_con_iva", nullable = false)
    private Double totalConIva;

    @Column(name = "numero_ticket", nullable = false, unique = true)
    private String numeroTicket;

    @Column(nullable = false)
    private String estado = "pendiente";

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("venta-detalles")
    private List<DetalleVenta> detallesVenta;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = new Date();
        }
        if (numeroTicket == null || numeroTicket.isEmpty()) {
            numeroTicket = "TICKET-" + UUID.randomUUID().toString().substring(0, 8);
        }
        if (estado == null) {
            estado = "pendiente";
        }
    }

    public Venta() {
    }

    public Venta(Cliente cliente, Almacen almacen, double totalSinIva, double totalConIva, String numeroTicket,
            String estado, Date fecha) {
        this.cliente = cliente;
        this.almacen = almacen;
        this.totalSinIva = totalSinIva;
        this.totalConIva = totalConIva;
        this.numeroTicket = numeroTicket;
        this.estado = estado;
        this.fecha = fecha;
    }
}
