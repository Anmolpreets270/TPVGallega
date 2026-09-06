package com.tpvgallega.backend.infrastructure.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.tpvgallega.backend.application.port.out.NotificacionPort;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.domain.model.TipoProducto;
import com.tpvgallega.backend.infrastructure.websocket.dto.LineaPedidoNotificacion;
import com.tpvgallega.backend.infrastructure.websocket.dto.PedidoNotificacion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificacionWebSocketAdapter implements NotificacionPort {

    private static final String TOPIC_COCINA = "/topic/cocina";
    private static final String TOPIC_BARRA = "/topic/barra";

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notificarNuevoPedido(Pedido pedido) {
        publicarEnZonasCorrespondientes(pedido);
    }

    @Override
    public void notificarCambioEstado(Pedido pedido) {
        publicarEnZonasCorrespondientes(pedido);
    }

    private void publicarEnZonasCorrespondientes(Pedido pedido) {
        PedidoNotificacion notificacion = toNotificacion(pedido);
        if (pedido.tieneLineasDeTipo(TipoProducto.COMIDA)) {
            messagingTemplate.convertAndSend(TOPIC_COCINA, notificacion);
        }
        if (pedido.tieneLineasDeTipo(TipoProducto.BEBIDA)) {
            messagingTemplate.convertAndSend(TOPIC_BARRA, notificacion);
        }
    }

    private PedidoNotificacion toNotificacion(Pedido pedido) {
        return new PedidoNotificacion(
                pedido.getId(),
                pedido.getMesa(),
                pedido.getEstado(),
                pedido.getFechaCreacion(),
                pedido.getFechaActualizacion(),
                pedido.getLineas().stream()
                        .map(linea -> new LineaPedidoNotificacion(
                                linea.getNombreProducto(),
                                linea.getCantidad(),
                                linea.getTipoProducto(),
                                linea.getNotas()))
                        .toList());
    }
}
