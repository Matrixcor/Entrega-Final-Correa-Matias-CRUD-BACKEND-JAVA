package com.techlab.crud.service;

import com.techlab.crud.model.Articulo.Articulo;
import com.techlab.crud.model.Pedido.DetallePedido;
import com.techlab.crud.model.Pedido.Pedido;
import com.techlab.crud.repository.Articulo.ArticuloRepository;
import com.techlab.crud.repository.Pedido.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        
        Double totalFinal = 0.0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            Objects.requireNonNull(detalle.getArticulo(), "DetallePedido.articulo no puede ser nulo");
            Long articuloId = Objects.requireNonNull(detalle.getArticulo().getId(), "Artículo.id no puede ser nulo");
            Articulo articuloDB = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new IllegalArgumentException("Artículo con ID " + articuloId + " no encontrado."));
            
            // B. VALIDACIÓN DE STOCK (Lógica de Negocio)
            // if (articuloDB.getStock() < detalle.getCantidad()) { ... throw ... }
            
            detalle.setPrecioUnidad(articuloDB.getPrecio().doubleValue()); 
            detalle.setPedido(pedido);
            totalFinal += detalle.getPrecioUnidad() * detalle.getCantidad();
        }
        pedido.setTotal(totalFinal);
        return pedidoRepository.save(pedido); 
    }

    // otros métodos (save)
    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findByEstadoNot("CANCELADO");
    }
    
    @Override
    public Optional<Pedido> findById(Long id) {
        Objects.requireNonNull(id, "id no puede ser nulo");
        return pedidoRepository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
        // 1. Opcional: Podrías añadir lógica de negocio aquí 
        //    (ej. verificar el estado del pedido antes de eliminarlo).
    
        // 2. Delegar la persistencia al Repositorio
        Objects.requireNonNull(id, "id no puede ser nulo");
        pedidoRepository.deleteById(id);
    }
}