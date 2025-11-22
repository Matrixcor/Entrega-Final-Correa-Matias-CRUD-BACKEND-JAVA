package com.techlab.crud.repository.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.crud.model.Categoria.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Inherits Optional<Categoria> findById(Long) from JpaRepository — no override needed
}