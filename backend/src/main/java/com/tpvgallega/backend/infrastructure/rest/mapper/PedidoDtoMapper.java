package com.tpvgallega.backend.infrastructure.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.rest.generated.model.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.generated.model.LineaPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.generated.model.LineaPedidoResponse;
import com.tpvgallega.backend.infrastructure.rest.generated.model.PedidoResponse;

@Mapper(componentModel = "spring")
public interface PedidoDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Pedido toDomain(CrearPedidoRequest request);

    @Mapping(target = "id", ignore = true)
    LineaPedido toDomain(LineaPedidoRequest request);

    PedidoResponse toResponse(Pedido pedido);

    LineaPedidoResponse toResponse(LineaPedido linea);

    com.tpvgallega.backend.domain.model.EstadoPedido toDomain(
            com.tpvgallega.backend.infrastructure.rest.generated.model.EstadoPedido estado);
}
