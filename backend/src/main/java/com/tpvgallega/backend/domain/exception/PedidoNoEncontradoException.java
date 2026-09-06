package com.tpvgallega.backend.domain.exception;

public class PedidoNoEncontradoException extends RuntimeException {

    public PedidoNoEncontradoException(Long id) {
        super("No existe ningun pedido con id " + id);
    }
}
