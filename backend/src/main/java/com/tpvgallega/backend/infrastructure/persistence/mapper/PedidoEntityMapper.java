package com.tpvgallega.backend.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.persistence.LineaPedidoJpaEntity;
import com.tpvgallega.backend.infrastructure.persistence.PedidoJpaEntity;

@Component
public class PedidoEntityMapper {

    public PedidoJpaEntity toEntity(Pedido pedido) {
        PedidoJpaEntity entity = PedidoJpaEntity.builder()
                .id(pedido.getId())
                .mesa(pedido.getMesa())
                .estado(pedido.getEstado())
                .fechaCreacion(pedido.getFechaCreacion())
                .fechaActualizacion(pedido.getFechaActualizacion())
                .build();

        List<LineaPedidoJpaEntity> lineas = pedido.getLineas().stream()
                .map(linea -> toLineaEntity(linea, entity))
                .toList();
        entity.setLineas(lineas);
        return entity;
    }

    public Pedido toDomain(PedidoJpaEntity entity) {
        return Pedido.builder()
                .id(entity.getId())
                .mesa(entity.getMesa())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .lineas(entity.getLineas().stream().map(this::toLineaDomain).toList())
                .build();
    }

    private LineaPedidoJpaEntity toLineaEntity(LineaPedido linea, PedidoJpaEntity pedidoEntity) {
        return LineaPedidoJpaEntity.builder()
                .id(linea.getId())
                .nombreProducto(linea.getNombreProducto())
                .cantidad(linea.getCantidad())
                .tipoProducto(linea.getTipoProducto())
                .notas(linea.getNotas())
                .pedido(pedidoEntity)
                .build();
    }

    private LineaPedido toLineaDomain(LineaPedidoJpaEntity entity) {
        return LineaPedido.builder()
                .id(entity.getId())
                .nombreProducto(entity.getNombreProducto())
                .cantidad(entity.getCantidad())
                .tipoProducto(entity.getTipoProducto())
                .notas(entity.getNotas())
                .build();
    }
}
