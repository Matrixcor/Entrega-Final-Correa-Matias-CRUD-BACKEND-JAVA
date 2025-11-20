package com.techlab.crud.service;

import com.techlab.crud.model.Articulo;
import java.util.List;
import java.util.Optional;

public interface ArticuloService {
    Articulo save(Articulo articulo);
    List<Articulo> findAll();
    Optional<Articulo> findById(Long id);
    
    void deleteById(Long id);
}