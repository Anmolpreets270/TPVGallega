package com.tpvgallega.backend.infrastructure.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.rest.dto.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.LineaPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.LineaPedidoResponse;
import com.tpvgallega.backend.infrastructure.rest.dto.PedidoResponse;

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
}
