package com.techlab.crud.controller;

import com.techlab.crud.service.ArticuloService.ArticuloService;
import com.techlab.crud.dto.Articulo.ArticuloRequestDTO;
import com.techlab.crud.dto.Articulo.ArticuloResponseDTO;
import com.techlab.crud.exception.CategoriaNoEncontradaException;
import com.techlab.crud.exception.ArticuloNoEncontradoException;
import com.techlab.crud.mapper.ArticuloMapper;
import com.techlab.crud.model.articulo.Articulo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/articulos")

public class ArticulosController {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private ArticuloMapper articuloMapper; 

    @PostMapping
    public ResponseEntity<?> crearArticulo(@RequestBody ArticuloRequestDTO articuloDTO) {
        try {
            Articulo articulo = articuloMapper.toEntity(articuloDTO); 
            Articulo nuevoArticulo = articuloService.save(articulo);
            ArticuloResponseDTO responseDTO = articuloMapper.toDto(nuevoArticulo);
            
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{aid}")
    public ResponseEntity<ArticuloResponseDTO> obtenerPorId(@PathVariable Long aid) {
        return articuloService.findById(aid)
            .map(articulo -> articuloMapper.toDto(articulo))
            .map(responseDTO -> new ResponseEntity<>(responseDTO, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ArticuloResponseDTO>> obtenerTodosOBuscar(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String marca) {

        List<Articulo> articulos;

        if (nombre != null && !nombre.isEmpty()) {
            articulos = articuloService.findByNombre(nombre);
        
        } else if (marca != null && !marca.isEmpty()) {
            articulos = articuloService.findByMarca(marca);
        } else {
            articulos = articuloService.findAll();
        }
        List<ArticuloResponseDTO> responseList = articuloMapper.toDto(articulos);
        if (responseList.isEmpty() && (nombre != null || marca != null)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    /* PARA  FUNCIONES DE ADMINISTRADOS ECOMMERCE 
       SE DEJA ESTA OPCION POR SI EL PROYECTO ESCALA A FUTURO, CLARO
       CON MODIFICACIONES DE ENDPOINTS SEGURO
    */

    @PutMapping("/{aid}")
    public ResponseEntity<?> updateArticulo(
            @PathVariable Long aid, 
            @RequestBody ArticuloRequestDTO articuloDTO) {
        
        try {
            Articulo articuloDetails = articuloMapper.toEntity(articuloDTO); 
            Articulo articuloActualizado = articuloService.update(aid, articuloDetails);
            ArticuloResponseDTO responseDTO = articuloMapper.toDto(articuloActualizado);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{aid}")
    public ResponseEntity<ArticuloResponseDTO> eliminarArticuloLogico(@PathVariable Long aid) {
       try {
            Articulo articuloEliminado = articuloService.deleteById(aid);
            ArticuloResponseDTO responseDTO = articuloMapper.toDto(articuloEliminado);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ArticuloNoEncontradoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        } 
    }
}