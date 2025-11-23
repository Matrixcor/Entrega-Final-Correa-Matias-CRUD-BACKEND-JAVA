package com.techlab.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticuloNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ArticuloNoEncontradoException(Long articuloId) {
        super("El artículo con ID " + articuloId + " no fue encontrado o está inactivo.");
    }
}
