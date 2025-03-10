package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolDTO {
    private Long id;
    private String nombre;

    public RolDTO() {}

    public RolDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
