package com.techlab.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String nombreArticulo, Integer stockDisponible, Integer stockSolicitado) {
        super("Stock insuficiente para el artículo '" + nombreArticulo + 
              "'. Solicitado: " + stockSolicitado + ", Disponible: " + stockDisponible);
    }
}