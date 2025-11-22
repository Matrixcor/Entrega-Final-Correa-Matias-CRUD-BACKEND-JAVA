package com.techlab.crud.service;

import com.techlab.crud.model.Articulo.Articulo;
import com.techlab.crud.model.Categoria.Categoria;
import com.techlab.crud.repository.Articulo.ArticuloRepository;

import com.techlab.crud.repository.Categoria.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository repository;
    
    @Autowired 
    private CategoriaRepository categoriaRepository;
    
    @Override
    @Transactional
    public Articulo save(Articulo articulo) {
        // Validate precio and stock presence before numeric comparisons
        if (articulo.getPrecio() == null) {
            throw new IllegalArgumentException("El precio no puede ser nulo.");
        }
        if (articulo.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        if (articulo.getStock() == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo.");
        }
        if (articulo.getCategoria() == null || articulo.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría válida.");
        } else {
            Long categoriaId = Objects.requireNonNull(articulo.getCategoria().getId(), "Categoria.id no puede ser nulo");
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
            // protect against null stock values
            int existenteStock = existente.getStock() == null ? 0 : existente.getStock();
            int nuevoStock = articulo.getStock() == null ? 0 : articulo.getStock();
            existente.setStock(existenteStock + nuevoStock);
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
        Objects.requireNonNull(id, "id no puede ser nulo");
        return repository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Articulo articulo = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
        articulo.setActivo(false);
        repository.save(articulo);
    }         
}