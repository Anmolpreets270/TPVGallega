package com.tpvgallega.backend.infrastructure.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.tpvgallega.backend.application.port.out.NotificacionPort;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.domain.model.TipoProducto;
import com.tpvgallega.backend.infrastructure.websocket.dto.PedidoNotificacion;
import com.tpvgallega.backend.infrastructure.websocket.mapper.PedidoNotificacionMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificacionWebSocketAdapter implements NotificacionPort {

    private static final String TOPIC_COCINA = "/topic/cocina";
    private static final String TOPIC_BARRA = "/topic/barra";

    private final SimpMessagingTemplate messagingTemplate;
    private final PedidoNotificacionMapper mapper;

    @Override
    public void notificarNuevoPedido(Pedido pedido) {
        publicarEnZonasCorrespondientes(pedido);
    }

    @Override
    public void notificarCambioEstado(Pedido pedido) {
        publicarEnZonasCorrespondientes(pedido);
    }

    private void publicarEnZonasCorrespondientes(Pedido pedido) {
        PedidoNotificacion notificacion = mapper.toNotificacion(pedido);
        if (pedido.tieneLineasDeTipo(TipoProducto.COMIDA)) {
            messagingTemplate.convertAndSend(TOPIC_COCINA, notificacion);
        }
        if (pedido.tieneLineasDeTipo(TipoProducto.BEBIDA)) {
            messagingTemplate.convertAndSend(TOPIC_BARRA, notificacion);
        }
    }
}
