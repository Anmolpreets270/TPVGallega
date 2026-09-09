package com.tpvgallega.backend.infrastructure.persistence.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.persistence.LineaPedidoJpaEntity;
import com.tpvgallega.backend.infrastructure.persistence.PedidoJpaEntity;

@Mapper(componentModel = "spring")
public interface PedidoEntityMapper {

    PedidoJpaEntity toEntity(Pedido pedido);

    @Mapping(target = "pedido", ignore = true)
    LineaPedidoJpaEntity toEntity(LineaPedido linea);

    Pedido toDomain(PedidoJpaEntity entity);

    LineaPedido toDomain(LineaPedidoJpaEntity entity);

    @AfterMapping
    default void enlazarLineasConPedido(@MappingTarget PedidoJpaEntity entity) {
        entity.getLineas().forEach(linea -> linea.setPedido(entity));
    }
}
