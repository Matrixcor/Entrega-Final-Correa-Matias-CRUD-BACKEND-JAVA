package com.techlab.crud.dto.Articulo;

import com.techlab.crud.dto.CategoriaResponseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticuloResponseDTO {

    private Long id;
    private String nombre;
    private String marca;
    private Double precio;
    private Integer stock;
    private Boolean activo;
    
   private CategoriaResponseDTO categoria;
}