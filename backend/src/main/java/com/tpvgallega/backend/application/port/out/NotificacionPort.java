package com.tpvgallega.backend.application.port.out;

import com.tpvgallega.backend.domain.model.Pedido;

public interface NotificacionPort {

    void notificarNuevoPedido(Pedido pedido);

    void notificarCambioEstado(Pedido pedido);
}
