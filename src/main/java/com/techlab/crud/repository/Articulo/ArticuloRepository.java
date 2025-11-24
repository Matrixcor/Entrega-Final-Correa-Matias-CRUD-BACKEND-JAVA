package com.techlab.crud.repository.Articulo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.crud.model.articulo.Articulo;

import java.util.List;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    
    List<Articulo> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    List<Articulo> findByMarcaContainingIgnoreCaseAndActivoTrue(String marca);
    List<Articulo> findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCaseAndActivoTrue(String nombre, String marca);
    List<Articulo> findByActivoTrue();
    List<Articulo> findByActivoFalse();
    List<Articulo> findByNombreContainingAndActivoTrue(String nombre);
    List<Articulo> findByCategoriaIdAndActivoTrue(Long categoriaId);
    List<Articulo> findByPrecioLessThanEqualAndActivoTrue(Double precio);
    
    Optional<Articulo> findByNombreAndMarca(String nombre, String marca);
}