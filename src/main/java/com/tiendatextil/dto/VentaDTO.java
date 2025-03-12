package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VentaDTO {
    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private String fecha;
    private Double totalSinIVA;
    private Double totalConIVA;
    private String numeroTicket;
    private String estado;
    private List<DetalleVentaDTO> detalles; 

    // Constructor vacío necesario para Jackson
    public VentaDTO() {
    }
}