package com.techlab.crud.dto.Pedido;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.techlab.crud.dto.DetallePedido.DetallePedidoResponseDTO;

@Data
@NoArgsConstructor
public class PedidoResponseDTO implements Serializable {

    private Long id;
    private String nombreCliente;
    private Long clienteId;
    private String estado;
    private LocalDateTime fecha;
    private Double total;
    
    private List<DetallePedidoResponseDTO> detalles;
}