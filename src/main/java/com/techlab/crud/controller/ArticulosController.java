package com.techlab.crud.controller;

import com.techlab.crud.model.Articulo;
import com.techlab.crud.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/articulos")

public class ArticulosController {

    @Autowired
    private ArticuloService articuloService;

    @PostMapping
    public Articulo crearArticulo(@RequestBody Articulo articulo) {

        try {
            return articuloService.save(articulo);
        } catch (IllegalArgumentException e) {
            // 3. Maneja el error de validación del servicio y devuelve un error al cliente.
            // (En un caso real, usarías @ResponseStatus para devolver un 400 Bad Request)
            throw new RuntimeException("Error de validación: " + e.getMessage());
        }
    }
    
    @GetMapping 
    public List<Articulo> obtenerTodos() {
        return articuloService.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Articulo> obtenerPorId(@PathVariable Long id) {
        return articuloService.findById(id);
    }
}