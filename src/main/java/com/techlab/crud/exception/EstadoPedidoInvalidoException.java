package com.techlab.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstadoPedidoInvalidoException extends RuntimeException {
    
    public EstadoPedidoInvalidoException(String estadoInvalido) {
        super("El estado '" + estadoInvalido + "' no es un estado válido para un pedido.");
    }
}