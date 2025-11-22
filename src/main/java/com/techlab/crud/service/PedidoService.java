package com.techlab.crud.service;

import java.util.List;
import java.util.Optional;

import com.techlab.crud.model.Pedido.Pedido;

public interface PedidoService {
    Pedido save(Pedido pedido);
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    void deleteById(Long id);
}