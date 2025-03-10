package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Long idRol;
    private Date createdAt;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nombre, String email, String telefono, Long idRol, Date createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.idRol = idRol;
        this.createdAt = createdAt;
    }
}
