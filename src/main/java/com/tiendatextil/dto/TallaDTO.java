package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TallaDTO {
    private Long id;
    private String talla;

    public TallaDTO() {}

    public TallaDTO(Long id, String talla) {
        this.id = id;
        this.talla = talla;
    }
}
