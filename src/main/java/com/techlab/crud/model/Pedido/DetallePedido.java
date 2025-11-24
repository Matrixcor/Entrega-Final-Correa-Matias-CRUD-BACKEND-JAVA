package com.techlab.crud.model.Pedido;

//import com.techlab.crud.model.Articulo.Articulo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreArticulo;
    private String marcaArticulo;
    private Double precioUnidad; 
    private Integer cantidad;
    private Double subtotal;

    @Column(name = "articulo_id", nullable = false)
    private Long articuloId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
