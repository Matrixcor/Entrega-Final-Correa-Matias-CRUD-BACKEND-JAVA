package com.techlab.crud.controller;

import com.techlab.crud.dto.Pedido.PedidoRequestDTO;
import com.techlab.crud.dto.Pedido.PedidoResponseDTO;
import com.techlab.crud.dto.EstadoPedidoDTO;
import com.techlab.crud.exception.ArticuloNoEncontradoException;
import com.techlab.crud.exception.StockInsuficienteException;
import com.techlab.crud.model.Pedido.Pedido;
import com.techlab.crud.mapper.PedidoMapper; 
import com.techlab.crud.service.PedidoService.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos") 
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoRequestDTO pedidoDTO) {
        try {
            Pedido pedidoCreado = pedidoService.crearPedido(pedidoDTO);
            PedidoResponseDTO responseDTO = pedidoMapper.toDto(pedidoCreado);
            
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            
        } catch (ArticuloNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() { 
        List<Pedido> pedidos = pedidoService.findAll();
        List<PedidoResponseDTO> responseList = pedidos.stream()
            .map(pedidoMapper::toDto)
            .collect(Collectors.toList());
            
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    
    @GetMapping("/{pid}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Long pid) {
        return pedidoService.findById(pid)
            .map(pedidoMapper::toDto)
            .map(responseDTO -> new ResponseEntity<>(responseDTO, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PatchMapping("/{pid}/estado")
    public ResponseEntity<PedidoResponseDTO> actualizarEstadoPedido(
            @PathVariable Long pid, 
            @RequestBody EstadoPedidoDTO estadoDTO) {
            
        Pedido pedidoActualizado = pedidoService.updateEstado(pid, estadoDTO.getEstado());
        PedidoResponseDTO responseDTO = pedidoMapper.toDto(pedidoActualizado);
        
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long pid) {
        pedidoService.deleteById(pid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }
}