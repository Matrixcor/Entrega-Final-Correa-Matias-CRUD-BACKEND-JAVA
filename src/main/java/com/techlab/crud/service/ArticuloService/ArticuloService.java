package com.techlab.crud.service.ArticuloService;

import java.util.List;
import java.util.Optional;

import com.techlab.crud.model.articulo.Articulo;

public interface ArticuloService {
    
    List<Articulo> findByNombre(String nombre);
    List<Articulo> findByMarca(String marca);
    
    List<Articulo> findAll();
    Optional<Articulo> findById(Long id);

    Articulo save(Articulo articulo);
    Articulo update(Long id, Articulo articulo);
    Articulo deleteById(Long id);
    
    void descontarStock(Long articuloId, Integer cantidad);
    void sumarStock(Long articuloId, Integer cantidad);
}