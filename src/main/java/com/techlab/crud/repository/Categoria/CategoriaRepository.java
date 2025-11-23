package com.techlab.crud.repository.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.crud.model.Categoria.Categoria;
import java.util.List;
//import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    List<Categoria> findByActivoTrue();
    //Optional <Categoria>findById(Long id);
}