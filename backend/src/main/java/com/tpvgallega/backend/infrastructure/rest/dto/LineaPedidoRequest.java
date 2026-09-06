package com.tpvgallega.backend.infrastructure.rest.dto;

import com.tpvgallega.backend.domain.model.TipoProducto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LineaPedidoRequest(
        @NotBlank String nombreProducto,
        int cantidad,
        @NotNull TipoProducto tipoProducto,
        String notas) {
}
