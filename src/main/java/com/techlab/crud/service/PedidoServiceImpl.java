package com.techlab.crud.service;

import com.techlab.crud.model.Articulo;
import com.techlab.crud.model.DetallePedido;
import com.techlab.crud.model.Pedido;
import com.techlab.crud.repository.Articulo.ArticuloRepository;
import com.techlab.crud.repository.Pedido.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ArticuloRepository articuloRepository; // Necesario para obtener el precio real

    @Override
    @Transactional // LA TRANSACCIÓN ABARCA TODA LA OPERACIÓN COMPLEJA
    public Pedido save(Pedido pedido) {
        
        Double totalFinal = 0.0;

        // 1. VALIDACIÓN Y PREPARACIÓN DE DETALLES
        for (DetallePedido detalle : pedido.getDetalles()) {
            
            // A. Recuperar el artículo real de la BD para asegurar que existe
            Long articuloId = detalle.getArticulo().getId();
            Articulo articuloDB = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new IllegalArgumentException("Artículo con ID " + articuloId + " no encontrado."));
            
            // B. VALIDACIÓN DE STOCK (Lógica de Negocio)
            // if (articuloDB.getStock() < detalle.getCantidad()) { ... throw ... }
            
            // C. FIJAR PRECIO Y VINCULACIÓN
            // Se usa el precio actual del artículo, no el que el cliente pueda haber enviado en el JSON.
            detalle.setPrecioUnidad(articuloDB.getPrecio().doubleValue()); 
            
            // Vincular el detalle al pedido padre (¡CRUCIAL para JPA!)
            detalle.setPedido(pedido);
            
            // D. CALCULAR TOTAL
            totalFinal += detalle.getPrecioUnidad() * detalle.getCantidad();
        }

        // 2. FIJAR TOTAL Y ESTADO EN EL PEDIDO CABECERA
        pedido.setTotal(totalFinal);
        
        // 3. PERSISTENCIA EN CASCADA
        // Como Pedido tiene un CascadeType.ALL en su relación @OneToMany
        // con DetallePedido, al guardar el pedido padre, todos sus detalles se guardan automáticamente.
        return pedidoRepository.save(pedido); 
    }
    // Dentro de PedidoServiceImpl.java

    // ... otros métodos (save, findAll, findById) ...
    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findByEstadoNotCanceled("CANCELADO");
    }
    
    @Override
    public Optional<Pedido> findById(Long id) {
        // Delegar la búsqueda al repositorio
        return pedidoRepository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
        // 1. Opcional: Podrías añadir lógica de negocio aquí 
        //    (ej. verificar el estado del pedido antes de eliminarlo).
    
        // 2. Delegar la persistencia al Repositorio
        pedidoRepository.deleteById(id);
    }
}