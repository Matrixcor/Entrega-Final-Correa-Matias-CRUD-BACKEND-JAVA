package com.techlab.crud.dto.Articulo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ArticuloStockPrecioDTO implements Serializable {

    private Long articuloId;
    private String nombre; 
    private String marca; 
    private String imageUrl;
    private Double precio; 
    private Integer stock;
}