package com.tpvgallega.backend.application.port.in;

import java.util.List;

import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.Pedido;

public interface GestionarPedidoUseCase {

    Pedido crearPedido(Pedido pedido);

    Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado);

    Pedido obtenerPedido(Long pedidoId);

    List<Pedido> listarPedidos(EstadoPedido estado);
}
