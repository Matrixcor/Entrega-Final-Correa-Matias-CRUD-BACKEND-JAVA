package com.techlab.crud.repository.Categoria;

import com.techlab.crud.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional <Categoria>findById(Long id);
}