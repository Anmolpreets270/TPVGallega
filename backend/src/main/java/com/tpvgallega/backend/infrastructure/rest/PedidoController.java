package com.tpvgallega.backend.infrastructure.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tpvgallega.backend.application.port.in.GestionarPedidoUseCase;
import com.tpvgallega.backend.infrastructure.rest.generated.api.PedidosApi;
import com.tpvgallega.backend.infrastructure.rest.generated.model.CambiarEstadoRequest;
import com.tpvgallega.backend.infrastructure.rest.generated.model.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.generated.model.EstadoPedido;
import com.tpvgallega.backend.infrastructure.rest.generated.model.PedidoResponse;
import com.tpvgallega.backend.infrastructure.rest.mapper.PedidoDtoMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PedidoController implements PedidosApi {

    private final GestionarPedidoUseCase gestionarPedidoUseCase;
    private final PedidoDtoMapper mapper;

    @Override
    public ResponseEntity<PedidoResponse> crearPedido(CrearPedidoRequest crearPedidoRequest) {
        PedidoResponse response =
                mapper.toResponse(gestionarPedidoUseCase.crearPedido(mapper.toDomain(crearPedidoRequest)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<PedidoResponse>> listarPedidos(EstadoPedido estado) {
        List<PedidoResponse> pedidos = gestionarPedidoUseCase.listarPedidos(mapper.toDomain(estado)).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(pedidos);
    }

    @Override
    public ResponseEntity<PedidoResponse> obtenerPedido(Long id) {
        return ResponseEntity.ok(mapper.toResponse(gestionarPedidoUseCase.obtenerPedido(id)));
    }

    @Override
    public ResponseEntity<PedidoResponse> cambiarEstado(Long id, CambiarEstadoRequest cambiarEstadoRequest) {
        var nuevoEstado = mapper.toDomain(cambiarEstadoRequest.getEstado());
        PedidoResponse response = mapper.toResponse(gestionarPedidoUseCase.cambiarEstado(id, nuevoEstado));
        return ResponseEntity.ok(response);
    }
}
