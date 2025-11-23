package com.techlab.crud.service.CategoriaService;

import com.techlab.crud.model.Categoria.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    
    Categoria save(Categoria categoria);
    List<Categoria> findAllActivas();
    void deactivateById(Long id);
    Optional<Categoria> findById(Long id); 
}