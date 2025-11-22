package com.techlab.crud.service;

import java.util.List;
import java.util.Optional;

import com.techlab.crud.model.Pedido.Pedido;

public interface PedidoService {
    
    // Método principal para crear o actualizar
    Pedido save(Pedido pedido);
    
    // Métodos CRUD FALTANTES que el Controller necesita:
    List<Pedido> findAll();         // << ¡AÑADIR ESTE!
    Optional<Pedido> findById(Long id); // << ¡AÑADIR ESTE!
    void deleteById(Long id);       // << ¡AÑADIR ESTE!
}