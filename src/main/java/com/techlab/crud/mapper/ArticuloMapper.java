package com.techlab.crud.mapper;

import com.techlab.crud.dto.Articulo.ArticuloRequestDTO;
import com.techlab.crud.dto.Articulo.ArticuloResponseDTO;
import com.techlab.crud.model.articulo.Articulo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface ArticuloMapper {

    @Mapping(target = "id", ignore = true)

    Articulo toEntity(ArticuloRequestDTO dto);

    ArticuloResponseDTO toDto(Articulo articulo); 
    
    List<ArticuloResponseDTO> toDto(List<Articulo> articulos);
    
}
