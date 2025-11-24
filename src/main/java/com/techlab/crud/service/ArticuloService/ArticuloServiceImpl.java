package com.techlab.crud.service.ArticuloService;

import com.techlab.crud.repository.Articulo.ArticuloRepository;
import com.techlab.crud.repository.Categoria.CategoriaRepository;
import com.techlab.crud.model.categoria.Categoria;
import com.techlab.crud.exception.ArticuloNoEncontradoException;
import com.techlab.crud.exception.CategoriaNoEncontradaException;
import com.techlab.crud.exception.StockInsuficienteException;
import com.techlab.crud.model.articulo.Articulo;
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
    @Transactional("articuloTransactionManager")
    public Articulo save(Articulo articulo) {
    
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
            throw new IllegalArgumentException("El campo categoria no puede ser nulo o tener un ID nulo.");
        }

        Long categoriaId = articulo.getCategoria().getId();

        Objects.requireNonNull(categoriaId, "La categoria no puede ser nula.");
        Categoria categoriaCompleta = categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new CategoriaNoEncontradaException(categoriaId));

        articulo.setCategoria(categoriaCompleta);
        
        Optional<Articulo> articuloOpt = repository.findByNombreAndMarca(articulo.getNombre(), 
        articulo.getMarca());

        if (articuloOpt.isPresent()) {
            Articulo existente = articuloOpt.get();
            existente.setActivo(true);
            existente.setCategoria(categoriaCompleta); 
            existente.setPrecio(articulo.getPrecio());
            existente.setMarca(articulo.getMarca());
            int existenteStock = existente.getStock() == null ? 0 : existente.getStock();
            int nuevoStock = articulo.getStock() == null ? 0 : articulo.getStock();
            existente.setStock(existenteStock + nuevoStock);

            return repository.save(existente); 
        } else {
        
            return repository.save(articulo);
        }
    }

    @Override
    @Transactional("articuloTransactionManager")
    public void descontarStock(Long articuloId, Integer cantidad) {

        Objects.requireNonNull(articuloId, "El ID del artículo para disminuir stock no puede ser nulo.");
        Articulo articulo = repository.findById(articuloId)
            .orElseThrow(() -> new ArticuloNoEncontradoException(articuloId)); 
        
        Integer stockActual = Optional.ofNullable(articulo.getStock()).orElse(0);
        
        if (stockActual < cantidad) {
            throw new StockInsuficienteException(articulo.getNombre(), stockActual, cantidad); 
        }
        
        articulo.setStock(stockActual - cantidad);
        repository.save(articulo);
    }

    @Override
    @Transactional("articuloTransactionManager")
    public void sumarStock(Long articuloId, Integer cantidad) {

        Objects.requireNonNull(articuloId, "El ID del artículo para aumentar stock no puede ser nulo.");
        Articulo articulo = repository.findById(articuloId)
          
            .orElseThrow(() -> new ArticuloNoEncontradoException(articuloId)); 

        Integer stockActual = Optional.ofNullable(articulo.getStock()).orElse(0);
        
        articulo.setStock(stockActual + cantidad);
        repository.save(articulo);
    }
    
    @Override
    @Transactional("articuloTransactionManager")
    public List<Articulo> findAll() {
        return repository.findByActivoTrue();
    }
    @Override
    @Transactional("articuloTransactionManager")
    public Optional<Articulo> findById(Long id) {
        Objects.requireNonNull(id, "id no puede ser nulo");
        return repository.findById(id);
    }
    
    @Override
    @Transactional("articuloTransactionManager")
    public List<Articulo> findByNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);    
    }

    @Override
    @Transactional("articuloTransactionManager")
    public List<Articulo> findByMarca(String marca) {
        return repository.findByMarcaContainingIgnoreCaseAndActivoTrue(marca);
    }

    @Override
    @Transactional("articuloTransactionManager")
    public Articulo update(Long id, Articulo articulo) {
        
        Objects.requireNonNull(id, "El ID del artículo no puede ser nulo.");
        Articulo articuloExistente = repository.findById(id)
            .orElseThrow(() -> new ArticuloNoEncontradoException(id));

        if (articulo.getCategoria() == null || articulo.getCategoria().getId() == null) {
            throw new IllegalArgumentException("La categoría debe estar especificada para la actualización.");
        }
        Long categoriaId = articulo.getCategoria().getId();
        
        Objects.requireNonNull(categoriaId, "La categoria no puede ser nula.");
        Categoria categoriaCompleta = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new CategoriaNoEncontradaException(categoriaId));

        if (articulo.getPrecio() == null || articulo.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        if (articulo.getStock() == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo.");
        }
        
        articuloExistente.setNombre(articulo.getNombre());
        articuloExistente.setMarca(articulo.getMarca());
        articuloExistente.setPrecio(articulo.getPrecio());
        articuloExistente.setStock(articulo.getStock());
        articuloExistente.setActivo(articulo.getActivo()); 
        articuloExistente.setCategoria(categoriaCompleta);

        return repository.save(articuloExistente);
    }
    
    @Override
    @Transactional("articuloTransactionManager")
    public Articulo deleteById(Long id) {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Articulo articulo = repository.findById(id)
            .orElseThrow(() -> new ArticuloNoEncontradoException(id)); 
        articulo.setActivo(false);
        return repository.save(articulo);
    } 
}