package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class VentaDTO {
    private Long id;
    private Long idCliente;
    private Long idAlmacen;
    private double totalSinIva;
    private double totalConIva;
    private String numeroTicket;
    private String estado;
    private Date fecha;
    private List<DetalleVentaDTO> detallesVenta;

    public VentaDTO() {}

    public VentaDTO(Long id, Long idCliente, Long idAlmacen, double totalSinIva, double totalConIva, String numeroTicket, String estado, Date fecha, List<DetalleVentaDTO> detallesVenta) {
        this.id = id;
        this.idCliente = idCliente;
        this.idAlmacen = idAlmacen;
        this.totalSinIva = totalSinIva;
        this.totalConIva = totalConIva;
        this.numeroTicket = numeroTicket;
        this.estado = estado;
        this.fecha = fecha;
        this.detallesVenta = detallesVenta;
    }
}
