package com.techlab.crud.service;

import com.techlab.crud.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    
    // Método principal para crear o actualizar
    Pedido save(Pedido pedido);
    
    // Métodos CRUD FALTANTES que el Controller necesita:
    List<Pedido> findAll();         // << ¡AÑADIR ESTE!
    Optional<Pedido> findById(Long id); // << ¡AÑADIR ESTE!
    void deleteById(Long id);       // << ¡AÑADIR ESTE!
}