package com.techlab.crud.model;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

import lombok.Data; 
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@Entity 
public class Articulo {

    private Long id;
    private String nombre;
    private String marca;
    private Integer precio;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    private boolean activo = true;
    
    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    private List<DetallePedido> detallesPedido;   
}