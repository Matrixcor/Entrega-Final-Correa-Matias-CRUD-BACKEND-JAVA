package com.techlab.crud.controller;

import com.techlab.crud.model.Pedido;
import com.techlab.crud.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos") 
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarPedidos() { 
        return pedidoService.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        return pedidoService.findById(id);
    }
    
    @PostMapping
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return pedidoService.save(pedido);
    }
    
    @DeleteMapping("/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoService.deleteById(id);
    }
}