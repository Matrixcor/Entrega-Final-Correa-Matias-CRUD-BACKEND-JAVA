package com.techlab.crud.controller;

import com.techlab.crud.model.categoria.Categoria;
import com.techlab.crud.service.CategoriaService.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @GetMapping
    public List<Categoria> getAllCategoriasActivas() {
        return categoriaService.findAllActivas();
    }
   
    @GetMapping("/{cid}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long cid) {
        return categoriaService.findById(cid)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{cid}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long cid, @RequestBody Categoria categoria) {
        try {
            categoria.setId(cid);
            
            Categoria categoriaActualizada = categoriaService.save(categoria);
            
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al actualizar la categoría: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{cid}")
    public ResponseEntity<Void> deleteCategoriaLogica(@PathVariable Long cid) {
        try {
            categoriaService.deactivateById(cid);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}