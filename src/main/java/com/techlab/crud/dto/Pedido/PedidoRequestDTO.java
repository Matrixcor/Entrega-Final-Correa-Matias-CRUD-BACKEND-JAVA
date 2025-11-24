package com.techlab.crud.dto.Pedido;

import java.util.List;

import com.techlab.crud.dto.DetallePedido.DetallePedidoRequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PedidoRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio.")
    private String nombreCliente;

    @NotNull(message = "El ID del cliente es obligatorio.")
    private Long clienteId;

    @NotEmpty(message = "El pedido debe contener al menos un articulo.")
    private List<DetallePedidoRequestDTO> detalles;

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public Long getClienteId() { return clienteId; } 
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public List<DetallePedidoRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoRequestDTO> detalles) { this.detalles = detalles; }
}