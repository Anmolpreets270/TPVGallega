package com.tpvgallega.backend.infrastructure.websocket.dto;

import com.tpvgallega.backend.domain.model.TipoProducto;

public record LineaPedidoNotificacion(
        String nombreProducto,
        int cantidad,
        TipoProducto tipoProducto,
        String notas) {
}
