package com.techlab.crud.service;

import com.techlab.crud.model.Articulo;
import com.techlab.crud.repository.Articulo.ArticuloRepository;

import com.techlab.crud.repository.Categoria.CategoriaRepository;
import com.techlab.crud.model.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository repository;
    
    @Autowired 
    private CategoriaRepository categoriaRepository;
    
    @Override
    @Transactional
    public Articulo save(Articulo articulo) {
    
        if (articulo.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio no puede ser nulo.");
        }
        if (articulo.getCategoria() == null || articulo.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría válida.");
        }else{
            Long categoriaId = articulo.getCategoria().getId();
            Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
            if (categoriaOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría con ID " + categoriaId + " no fue encontrada.");
            }
        }
        //paso las validaciones, entonces veo si ya existe un articulo igual
        Optional<Articulo> articuloOpt = repository.findByNombreAndMarca(articulo.getNombre(), 
        articulo.getMarca());

        if (articuloOpt.isPresent()) {
            Articulo existente = articuloOpt.get();
            existente.setPrecio(articulo.getPrecio());
            existente.setMarca(articulo.getMarca());
            existente.setStock(existente.getStock() + articulo.getStock()); 
            return repository.save(existente);   
        } else {
            return repository.save(articulo);
        }
    }

    @Override
    public List<Articulo> findAll() {
        return repository.findByActivoTrue();
    }
    
    @Override
    public Optional<Articulo> findById(Long id) {
        return repository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
        Articulo articulo = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
        articulo.setActivo(false);
        repository.save(articulo);
    }         
}