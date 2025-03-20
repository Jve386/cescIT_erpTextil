package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;

    // Constructor vacío necesario para Jackson
    public ClienteDTO() {
    }
} 