package com.techlab.crud.repository.Articulo;

import com.techlab.crud.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    
    List<Articulo> findByActivoTrue();
    List<Articulo> findByActivoFalse();
    List<Articulo> findByNombreContainingAndActivoTrue(String nombre);
    List<Articulo> findByCategoriaIdAndActivoTrue(Long categoriaId);
    List<Articulo> findByPrecioLessThanEqualAndActivoTrue(int precio);

    Optional<Articulo> findByNombreAndMarca(String nombre, String marca);
}