package com.techlab.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class CategoriaNoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CategoriaNoEncontradaException(Long categoriaId) {
        super("La categoría con ID " + categoriaId + " no fue encontrada. Por favor, asegúrese de que la categoría exista antes de asociar un artículo.");
    }
}
