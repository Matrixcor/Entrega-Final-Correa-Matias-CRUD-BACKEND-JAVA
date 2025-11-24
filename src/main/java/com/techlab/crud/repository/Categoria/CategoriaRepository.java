package com.techlab.crud.repository.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.crud.model.categoria.Categoria;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    List<Categoria> findByActivoTrue();
}