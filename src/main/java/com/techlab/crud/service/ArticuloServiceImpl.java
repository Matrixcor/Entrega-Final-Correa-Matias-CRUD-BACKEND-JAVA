package com.techlab.crud.service;

import com.techlab.crud.model.Articulo;
import com.techlab.crud.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository repository;

    @Override
    @Transactional
    public Articulo save(Articulo articulo) {

        //buscaria el producto por el id para ver si ya existe
        // Antiguo ValidadorDatos se maneja aquí:
        if (articulo.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo.");
        }
        // Antiguo seleccionarOCrearCategoria() se maneja así:
        // Lógica de Negocio: Validación de la relación obligatoria
        if (articulo.getCategoria() == null || articulo.getCategoria().getId() == null) {
             // Si el formulario obliga a seleccionar, esto captura si no se seleccionó nada.
            throw new IllegalArgumentException("Debe seleccionar una categoría válida.");
        }
    
        // 3. Persistencia: Llama a JpaRepository para ejecutar el SQL INSERT/UPDATE.
        
        return repository.save(articulo);
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