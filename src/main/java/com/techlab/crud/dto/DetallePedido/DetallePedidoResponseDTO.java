package com.techlab.crud.dto.DetallePedido;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class DetallePedidoResponseDTO implements Serializable {
    
    private Long id; 
    private Long articuloId; 

    private String nombreArticulo;
    private String marcaArticulo;
    private String imageUrlArticulo;
    private Double precioUnidad; 
    private Integer cantidad;
    private Double subtotal;
}