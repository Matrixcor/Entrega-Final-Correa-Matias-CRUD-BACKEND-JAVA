package com.techlab.crud.service.ArticuloService; 

import com.techlab.crud.dto.Articulo.ArticuloStockPrecioDTO;
import com.techlab.crud.exception.ArticuloNoEncontradoException;

public interface ArticuloClienteService {

    ArticuloStockPrecioDTO getArticuloById(Long articuloId) throws ArticuloNoEncontradoException;

    void descontarStock(Long articuloId, Integer cantidad);
    void sumarStock(Long articuloId, Integer cantidad);
}