package com.tpvgallega.backend.infrastructure.websocket.mapper;

import org.mapstruct.Mapper;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.websocket.dto.LineaPedidoNotificacion;
import com.tpvgallega.backend.infrastructure.websocket.dto.PedidoNotificacion;

@Mapper(componentModel = "spring")
public interface PedidoNotificacionMapper {

    PedidoNotificacion toNotificacion(Pedido pedido);

    LineaPedidoNotificacion toNotificacion(LineaPedido linea);
}
