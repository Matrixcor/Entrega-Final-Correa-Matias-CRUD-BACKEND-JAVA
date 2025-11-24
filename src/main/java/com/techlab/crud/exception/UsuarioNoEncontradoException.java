package com.techlab.crud.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioNoEncontradoException(Long usuarioId) {
        super("El usuario con ID " + usuarioId + " no fue encontrado o está inactivo.");
    }
}