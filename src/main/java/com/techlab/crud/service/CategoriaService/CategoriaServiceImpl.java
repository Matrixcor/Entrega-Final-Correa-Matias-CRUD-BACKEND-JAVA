package com.techlab.crud.service.CategoriaService;

import org.springframework.stereotype.Service;
import com.techlab.crud.model.Categoria.Categoria;
import com.techlab.crud.repository.Categoria.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.lang.RuntimeException;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public Categoria save(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setActivo(true);
        }
        return categoriaRepository.save(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAllActivas() {
        return categoriaRepository.findByActivoTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> findById(Long id) {
        Objects.requireNonNull(id, "El ID de la categoría no puede estar vacio.");
        return categoriaRepository.findById(id);
    }
    
    @Override
    @Transactional
    public void deactivateById(Long id) {
        Objects.requireNonNull(id, "El ID de la categoría no puede estar vacio.");
        
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría con ID " + id + " no encontrada.")); 

        categoria.setActivo(false); 
        categoriaRepository.save(categoria);
    }
}