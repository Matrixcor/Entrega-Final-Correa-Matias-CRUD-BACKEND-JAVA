package com.techlab.crud.service.PedidoService;

import com.techlab.crud.model.Pedido.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido save(Pedido pedido);
    Pedido updateEstado(Long id, String nuevoEstado);
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    void deleteById(Long id);
}