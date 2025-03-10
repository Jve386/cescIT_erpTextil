package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlmacenDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String tipoAlmacen;

    public AlmacenDTO() {}

    public AlmacenDTO(Long id, String nombre, String direccion, String tipoAlmacen) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipoAlmacen = tipoAlmacen;
    }
}
