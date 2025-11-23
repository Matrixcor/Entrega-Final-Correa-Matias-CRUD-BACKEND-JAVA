package com.techlab.crud.service.PedidoService;

import com.techlab.crud.model.Pedido.Pedido;
import com.techlab.crud.model.Pedido.DetallePedido;
import com.techlab.crud.model.Articulo.Articulo;
import com.techlab.crud.repository.Pedido.PedidoRepository;
import com.techlab.crud.service.ArticuloService.ArticuloService;
import com.techlab.crud.exception.StockInsuficienteException;
import com.techlab.crud.exception.ArticuloNoEncontradoException;
import com.techlab.crud.exception.EstadoPedidoInvalidoException;
import com.techlab.crud.exception.PedidoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ArticuloService articuloService; 
    
    private static final List<String> ESTADOS_VALIDOS = List.of("PENDIENTE", "ENVIADO", "COMPLETADO", "CANCELADO");

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        double totalCalculado = 0.0;

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un artículo.");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() == null || detalle.getArticulo().getId() == null) {
                 throw new IllegalArgumentException("Cada detalle de pedido debe tener una referencia de Artículo válida.");
            }
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                 throw new IllegalArgumentException("La cantidad solicitada debe ser mayor que cero.");
            }
            

            Long articuloId = Objects.requireNonNull(detalle.getArticulo().getId(), 
            "El ID del artículo en el detalle no puede ser nulo.");

            Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new ArticuloNoEncontradoException(articuloId));
            
            Integer stockSolicitado = detalle.getCantidad();
        
            Integer stockDisponible = Optional.ofNullable(articulo.getStock()).orElse(0);

            if (stockSolicitado > stockDisponible) {
                throw new StockInsuficienteException(articulo.getNombre(), stockDisponible, stockSolicitado);
            }

            detalle.setPrecioUnidad(articulo.getPrecio());
            double subTotalCalculadoLocal = detalle.getCantidad() * articulo.getPrecio();
            totalCalculado += subTotalCalculadoLocal;

            detalle.setPedido(pedido);
            detalle.setArticulo(articulo); 
            articulo.setStock(stockDisponible - stockSolicitado);
            articuloService.save(articulo);
        }
        pedido.setTotal(totalCalculado);
        
        return pedidoRepository.save(pedido);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo");
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional
    public Pedido updateEstado(Long id, String nuevoEstado) {
        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo");
        String estadoUpper = nuevoEstado.toUpperCase(Locale.ROOT);
        if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
            throw new EstadoPedidoInvalidoException(nuevoEstado);
        }
        
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new PedidoNoEncontradoException(id));
        
        pedido.setEstado(estadoUpper);
        return pedidoRepository.save(pedido);
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo.");
    
        Pedido pedido = pedidoRepository.findById(id)
        .orElseThrow(() -> new PedidoNoEncontradoException(id));

        // 1. Validación de Estado para Cancelación
        if (pedido.getEstado().equals("COMPLETADO") || pedido.getEstado().equals("CANCELADO")) {
            // No permitimos cancelar si ya está completado o previamente cancelado
            throw new EstadoPedidoInvalidoException(
            "No se puede cancelar un pedido que ya está en estado: " + pedido.getEstado());
        }
    
        for (DetallePedido detalle : pedido.getDetalles()) {
        
            Long articuloId = detalle.getArticulo().getId();
            Articulo articuloAActualizar = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException(
                "Error interno: Artículo ID " + articuloId + " asociado al pedido no fue encontrado para el control de stock."
            ));

            Integer cantidadADevolver = detalle.getCantidad();
            Integer stockActual = Optional.ofNullable(articuloAActualizar.getStock()).orElse(0);
            articuloAActualizar.setStock(stockActual + cantidadADevolver);
            articuloService.save(articuloAActualizar);
        }
    
        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
    }
}