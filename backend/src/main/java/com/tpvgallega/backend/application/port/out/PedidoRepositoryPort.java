package com.tpvgallega.backend.application.port.out;

import java.util.List;
import java.util.Optional;

import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.Pedido;

public interface PedidoRepositoryPort {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> buscarTodos();

    List<Pedido> buscarPorEstado(EstadoPedido estado);
}
