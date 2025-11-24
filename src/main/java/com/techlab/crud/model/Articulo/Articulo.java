package com.techlab.crud.model.articulo;

import com.techlab.crud.model.categoria.Categoria;

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

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "categoria_id") 
    private Categoria categoria;
    
    private Boolean activo = true;
    
}