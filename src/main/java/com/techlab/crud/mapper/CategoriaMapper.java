package com.techlab.crud.mapper;

import com.techlab.crud.dto.CategoriaResponseDTO;
import com.techlab.crud.model.categoria.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaResponseDTO toDto(Categoria categoria);
}