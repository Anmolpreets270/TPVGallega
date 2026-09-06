package com.tpvgallega.backend.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.tpvgallega.backend.domain.model.EstadoPedido;

public record PedidoResponse(
        Long id,
        int mesa,
        EstadoPedido estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        List<LineaPedidoResponse> lineas) {
}
