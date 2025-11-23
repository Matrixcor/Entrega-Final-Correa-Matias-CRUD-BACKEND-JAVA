package com.techlab.crud.model.Articulo;

import java.util.List;
import com.techlab.crud.model.Categoria.Categoria;
import com.techlab.crud.model.Pedido.DetallePedido;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String marca;
    private Double precio;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private boolean activo = true;

    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    private List<DetallePedido> detallesPedido;
}