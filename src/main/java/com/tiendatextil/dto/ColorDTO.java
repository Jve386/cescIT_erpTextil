package com.tiendatextil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorDTO {
    private Long id;
    private String color;

    public ColorDTO() {}

    public ColorDTO(Long id, String color) {
        this.id = id;
        this.color = color;
    }
}
