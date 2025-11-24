package com.techlab.crud.service.PedidoService;

import com.techlab.crud.dto.Pedido.PedidoRequestDTO;
import com.techlab.crud.model.Pedido.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crearPedido(PedidoRequestDTO pedidoDTO);
    Pedido updateEstado(Long id, String nuevoEstado);

    //opcional, antes de refactorizar cuando utilicemos JWT
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    void deleteById(Long id);
    
}