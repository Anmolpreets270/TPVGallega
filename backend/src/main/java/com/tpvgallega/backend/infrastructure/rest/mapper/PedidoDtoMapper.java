package com.tpvgallega.backend.infrastructure.rest.mapper;

import org.springframework.stereotype.Component;

import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.rest.dto.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.LineaPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.LineaPedidoResponse;
import com.tpvgallega.backend.infrastructure.rest.dto.PedidoResponse;

@Component
public class PedidoDtoMapper {

    public Pedido toDomain(CrearPedidoRequest request) {
        return Pedido.builder()
                .mesa(request.mesa())
                .lineas(request.lineas().stream().map(this::toDomain).toList())
                .build();
    }

    public PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getMesa(),
                pedido.getEstado(),
                pedido.getFechaCreacion(),
                pedido.getFechaActualizacion(),
                pedido.getLineas().stream().map(this::toResponse).toList());
    }

    private LineaPedido toDomain(LineaPedidoRequest request) {
        return LineaPedido.builder()
                .nombreProducto(request.nombreProducto())
                .cantidad(request.cantidad())
                .tipoProducto(request.tipoProducto())
                .notas(request.notas())
                .build();
    }

    private LineaPedidoResponse toResponse(LineaPedido linea) {
        return new LineaPedidoResponse(
                linea.getId(),
                linea.getNombreProducto(),
                linea.getCantidad(),
                linea.getTipoProducto(),
                linea.getNotas());
    }
}
