package com.techlab.crud.mapper;

import com.techlab.crud.dto.DetallePedido.DetallePedidoResponseDTO;
import com.techlab.crud.dto.Pedido.PedidoResponseDTO;
import com.techlab.crud.model.Pedido.DetallePedido;
import com.techlab.crud.model.Pedido.Pedido;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    
    PedidoResponseDTO toDto(Pedido pedido);
    DetallePedidoResponseDTO toDto(DetallePedido detallePedido);
    
}