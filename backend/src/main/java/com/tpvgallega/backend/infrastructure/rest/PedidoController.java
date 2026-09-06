package com.tpvgallega.backend.infrastructure.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tpvgallega.backend.application.port.in.GestionarPedidoUseCase;
import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.infrastructure.rest.dto.CambiarEstadoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.PedidoResponse;
import com.tpvgallega.backend.infrastructure.rest.mapper.PedidoDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final GestionarPedidoUseCase gestionarPedidoUseCase;
    private final PedidoDtoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crearPedido(@Valid @RequestBody CrearPedidoRequest request) {
        return mapper.toResponse(gestionarPedidoUseCase.crearPedido(mapper.toDomain(request)));
    }

    @GetMapping
    public List<PedidoResponse> listarPedidos(@RequestParam(required = false) EstadoPedido estado) {
        return gestionarPedidoUseCase.listarPedidos(estado).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse obtenerPedido(@PathVariable Long id) {
        return mapper.toResponse(gestionarPedidoUseCase.obtenerPedido(id));
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoRequest request) {
        return mapper.toResponse(gestionarPedidoUseCase.cambiarEstado(id, request.estado()));
    }
}
