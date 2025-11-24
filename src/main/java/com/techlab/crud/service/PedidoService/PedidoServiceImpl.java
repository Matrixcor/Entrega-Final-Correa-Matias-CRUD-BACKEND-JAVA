package com.techlab.crud.service.PedidoService;

import com.techlab.crud.model.Pedido.Pedido;
import com.techlab.crud.model.Pedido.DetallePedido;
import com.techlab.crud.repository.Pedido.PedidoRepository;
import com.techlab.crud.repository.Usuario.UserRepository;
import com.techlab.crud.service.ArticuloService.ArticuloClienteService;
import com.techlab.crud.dto.Articulo.ArticuloStockPrecioDTO;
import com.techlab.crud.dto.DetallePedido.DetallePedidoRequestDTO;
import com.techlab.crud.dto.Pedido.PedidoRequestDTO;
import com.techlab.crud.exception.StockInsuficienteException;
import com.techlab.crud.exception.UsuarioNoEncontradoException;
import com.techlab.crud.exception.PedidoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ArticuloClienteService articuloClienteService;

    private static final List<String> ESTADOS_VALIDOS = List.of("PENDIENTE", "ENVIADO", "COMPLETADO", "CANCELADO");

    @Override
    @Transactional("pedidosTransactionManager")
    public Pedido crearPedido(PedidoRequestDTO pedidoDTO) {
        
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setNombreCliente(pedidoDTO.getNombreCliente());
        nuevoPedido.setClienteId(pedidoDTO.getClienteId());
        
        double totalCalculado = 0.0;
        List<DetallePedido> detalles = new ArrayList<>();

        if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un artículo.");
        }
        
        for (DetallePedidoRequestDTO detalleDTO : pedidoDTO.getDetalles()) { 
            Long articuloId = detalleDTO.getArticuloId(); 
            Integer stockSolicitado = detalleDTO.getCantidad();

            ArticuloStockPrecioDTO articuloData = articuloClienteService.getArticuloById(articuloId); 
            Integer stockDisponible = Optional.ofNullable(articuloData.getStock()).orElse(0);

            if (stockSolicitado > stockDisponible) {
                throw new StockInsuficienteException(articuloData.getNombre(), stockDisponible, stockSolicitado);
            }

            DetallePedido detalle = new DetallePedido();

            detalle.setArticuloId(articuloData.getArticuloId());
            detalle.setPrecioUnidad(articuloData.getPrecio());
            detalle.setNombreArticulo(articuloData.getNombre());
            detalle.setMarcaArticulo(articuloData.getMarca());
            detalle.setImageUrlArticulo(articuloData.getImageUrl());
            detalle.setCantidad(stockSolicitado);
            
            double subTotalCalculadoLocal = stockSolicitado * articuloData.getPrecio();
            detalle.setSubtotal(subTotalCalculadoLocal);
            detalle.setPedido(nuevoPedido);
            
            totalCalculado += subTotalCalculadoLocal;
            detalles.add(detalle);
        }
        
        for (DetallePedido detalle : detalles) {
            articuloClienteService.descontarStock(detalle.getArticuloId(), detalle.getCantidad()); 
        }

        nuevoPedido.setDetalles(detalles);
        nuevoPedido.setTotal(totalCalculado);
        nuevoPedido.setEstado("PENDIENTE"); 
        
        return pedidoRepository.save(nuevoPedido); 
    }
    
    @Override
    @Transactional("pedidosTransactionManager")
    public List<Pedido> findByClienteId(Long usuarioId) {
        
        Objects.requireNonNull(usuarioId, "El ID del usuario no puede ser nulo");
        if (!userRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        return pedidoRepository.findByClienteId(usuarioId); 
    }

    @Override
    @Transactional("pedidosTransactionManager")
    public Optional<Pedido> findById(Long id) {
        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo");
        return pedidoRepository.findById(id); 
    }

    @Override
    @Transactional("pedidosTransactionManager")
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional("pedidosTransactionManager")
    public Pedido updateEstado(Long id, String nuevoEstado) {

        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo");
        String estadoUpper = nuevoEstado.toUpperCase(Locale.ROOT);
        
        if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
            throw new IllegalArgumentException("Estado de pedido inválido: " + nuevoEstado);
        }

        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new PedidoNoEncontradoException(id));

        if (!esTransicionValida(pedido.getEstado(), estadoUpper)) {
            throw new IllegalArgumentException("Transición inválida de " + pedido.getEstado() + " a " + estadoUpper);
        }
        pedido.setEstado(estadoUpper);
        return pedidoRepository.save(pedido);
    }
    
    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
        if (estadoActual.equals(nuevoEstado)) {
            return true;
        }
        switch (estadoActual) {
            case "PENDIENTE":
                return nuevoEstado.equals("ENVIADO") || nuevoEstado.equals("CANCELADO");
            case "ENVIADO":
                return nuevoEstado.equals("COMPLETADO") || nuevoEstado.equals("CANCELADO");
            case "COMPLETADO":
                return false; // No se puede cambiar un pedido completado (inmutable)
            case "CANCELADO":
                return false; // No se puede cambiar un pedido cancelado (inmutable)
            default:
                return false;
        }
    }

    @Override
    @Transactional("pedidosTransactionManager")
    public void deleteById(Long id) {

        Objects.requireNonNull(id, "El ID del pedido no puede ser nulo.");
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new PedidoNoEncontradoException(id));
            
        if (pedido.getEstado().equals("COMPLETADO")) {
            throw new IllegalArgumentException("No se puede eliminar/cancelar un pedido completado.");
        }

        if (!pedido.getEstado().equals("CANCELADO")) {
             for (DetallePedido detalle : pedido.getDetalles()) {
                Long articuloId = detalle.getArticuloId(); 
                Integer cantidadADevolver = detalle.getCantidad();
                articuloClienteService.sumarStock(articuloId, cantidadADevolver); 
            }
        }
        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
    }
}