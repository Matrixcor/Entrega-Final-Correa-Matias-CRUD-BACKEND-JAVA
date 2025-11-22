package com.techlab.crud;

import com.techlab.crud.model.Articulo.Articulo;
import com.techlab.crud.repository.Articulo.ArticuloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Usa application-test.properties
class ArticuloRepositoryTests {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Test
    void testSaveAndFindArticulo() {
        // Crear un artículo de prueba
        Articulo articulo = new Articulo();
        articulo.setNombre("Articulo Test");
        articulo.setPrecio(100.0);

        // Guardar
        articuloRepository.save(articulo);

        // Verificar que se guardó
        List<Articulo> articulos = articuloRepository.findAll();
        assertThat(articulos).hasSize(1);
        assertThat(articulos.get(0).getNombre()).isEqualTo("Articulo Test");
    }
}