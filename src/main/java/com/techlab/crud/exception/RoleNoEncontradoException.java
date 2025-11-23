package com.techlab.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class RoleNoEncontradoException extends RuntimeException {
    public RoleNoEncontradoException(Long roleId) {
        super("El rol con ID " + roleId + " no existe.");
    }
}
