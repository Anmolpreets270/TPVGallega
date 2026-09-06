package com.tpvgallega.backend.infrastructure.rest.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CrearPedidoRequest(
        int mesa,
        @NotEmpty @Valid List<LineaPedidoRequest> lineas) {
}
