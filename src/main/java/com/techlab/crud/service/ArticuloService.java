package com.techlab.crud.service;

import java.util.List;
import java.util.Optional;

import com.techlab.crud.model.Articulo.Articulo;

public interface ArticuloService {
    Articulo save(Articulo articulo);

    List<Articulo> findAll();
    
    Optional<Articulo> findById(Long id);
    
    void deleteById(Long id);
}