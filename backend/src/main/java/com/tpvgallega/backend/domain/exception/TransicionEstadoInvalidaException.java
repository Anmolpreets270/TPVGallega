package com.tpvgallega.backend.domain.exception;

import com.tpvgallega.backend.domain.model.EstadoPedido;

public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(EstadoPedido estadoActual, EstadoPedido estadoDestino) {
        super("No se puede pasar de %s a %s".formatted(estadoActual, estadoDestino));
    }
}
