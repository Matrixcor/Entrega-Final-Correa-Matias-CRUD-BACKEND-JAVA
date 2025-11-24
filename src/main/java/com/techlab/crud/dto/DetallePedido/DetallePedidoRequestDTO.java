package com.techlab.crud.dto.DetallePedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class DetallePedidoRequestDTO {

    @NotNull(message = "El ID del artículo es obligatorio.")
    private Long articuloId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidad;

    public Long getArticuloId() { return articuloId; }
    public void setArticuloId(Long articuloId) { this.articuloId = articuloId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}