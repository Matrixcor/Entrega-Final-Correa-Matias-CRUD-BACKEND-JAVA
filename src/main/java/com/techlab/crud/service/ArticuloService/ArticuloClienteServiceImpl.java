package com.techlab.crud.service.ArticuloService;

import com.techlab.crud.model.articulo.Articulo;
import com.techlab.crud.dto.Articulo.ArticuloStockPrecioDTO;
import com.techlab.crud.exception.ArticuloNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloClienteServiceImpl implements ArticuloClienteService {

    @Autowired
    private ArticuloService articuloService;

    @Override
    public ArticuloStockPrecioDTO getArticuloById(Long articuloId) {
        Articulo articulo = articuloService.findById(articuloId)
            .orElseThrow(() -> new ArticuloNoEncontradoException(articuloId));

        ArticuloStockPrecioDTO dto = new ArticuloStockPrecioDTO();
        dto.setArticuloId(articulo.getId());
        dto.setNombre(articulo.getNombre());
        dto.setMarca(articulo.getMarca());
        dto.setImageUrl(articulo.getImageUrl());
        dto.setStock(articulo.getStock());
        dto.setPrecio(articulo.getPrecio());
        
        return dto;
    }
    @Override
    public void sumarStock(Long articuloId, Integer cantidad) {
        articuloService.sumarStock(articuloId, cantidad); 
    }

    @Override
    public void descontarStock(Long articuloId, Integer cantidad) {
        articuloService.descontarStock(articuloId, cantidad);
    }
}