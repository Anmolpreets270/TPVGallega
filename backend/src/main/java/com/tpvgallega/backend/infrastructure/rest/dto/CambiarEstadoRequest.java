package com.tpvgallega.backend.infrastructure.rest.dto;

import com.tpvgallega.backend.domain.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(@NotNull EstadoPedido estado) {
}
