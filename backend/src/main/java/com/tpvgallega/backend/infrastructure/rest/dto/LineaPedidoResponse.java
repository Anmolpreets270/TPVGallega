package com.tpvgallega.backend.infrastructure.rest.dto;

import com.tpvgallega.backend.domain.model.TipoProducto;

public record LineaPedidoResponse(
        Long id,
        String nombreProducto,
        int cantidad,
        TipoProducto tipoProducto,
        String notas) {
}
