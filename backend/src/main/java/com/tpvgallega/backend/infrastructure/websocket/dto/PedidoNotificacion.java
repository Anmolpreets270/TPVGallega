package com.tpvgallega.backend.infrastructure.websocket.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.tpvgallega.backend.domain.model.EstadoPedido;

public record PedidoNotificacion(
        Long id,
        int mesa,
        EstadoPedido estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        List<LineaPedidoNotificacion> lineas) {
}
