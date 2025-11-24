package com.techlab.crud.repository.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlab.crud.model.Pedido.Pedido;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstadoNot(String estado);
    List<Pedido> findByClienteId(Long clienteId);

}
