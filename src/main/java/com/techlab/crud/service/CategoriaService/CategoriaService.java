package com.techlab.crud.service.CategoriaService;

import java.util.List;
import java.util.Optional;

import com.techlab.crud.model.categoria.Categoria;

public interface CategoriaService {
    
    Categoria save(Categoria categoria);
    List<Categoria> findAllActivas();
    void deactivateById(Long id);
    Optional<Categoria> findById(Long id); 
}