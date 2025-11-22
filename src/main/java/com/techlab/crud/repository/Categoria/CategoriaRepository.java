package com.techlab.crud.repository.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.crud.model.Categoria.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional <Categoria>findById(Long id);
}