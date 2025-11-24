package com.techlab.crud.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private Boolean activo;
}