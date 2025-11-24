package com.techlab.crud.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoPedidoDTO {

    @NotBlank(message = "El estado no puede ser vacío.")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}