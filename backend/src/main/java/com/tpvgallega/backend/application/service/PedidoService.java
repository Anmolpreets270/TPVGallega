package com.tpvgallega.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tpvgallega.backend.application.port.in.GestionarPedidoUseCase;
import com.tpvgallega.backend.application.port.out.NotificacionPort;
import com.tpvgallega.backend.application.port.out.PedidoRepositoryPort;
import com.tpvgallega.backend.domain.exception.PedidoInvalidoException;
import com.tpvgallega.backend.domain.exception.PedidoNoEncontradoException;
import com.tpvgallega.backend.domain.exception.TransicionEstadoInvalidaException;
import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService implements GestionarPedidoUseCase {

    private static final Map<EstadoPedido, Set<EstadoPedido>> TRANSICIONES_VALIDAS = Map.of(
            EstadoPedido.PENDIENTE, Set.of(EstadoPedido.PREPARANDO, EstadoPedido.CANCELADO),
            EstadoPedido.PREPARANDO, Set.of(EstadoPedido.LISTO, EstadoPedido.CANCELADO),
            EstadoPedido.LISTO, Set.of(EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO),
            EstadoPedido.ENTREGADO, Set.of(),
            EstadoPedido.CANCELADO, Set.of());

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final NotificacionPort notificacionPort;

    @Override
    public Pedido crearPedido(Pedido pedido) {
        validarMesa(pedido.getMesa());
        validarLineas(pedido.getLineas());

        LocalDateTime ahora = LocalDateTime.now();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(ahora);
        pedido.setFechaActualizacion(ahora);

        Pedido pedidoGuardado = pedidoRepositoryPort.guardar(pedido);
        notificacionPort.notificarNuevoPedido(pedidoGuardado);
        return pedidoGuardado;
    }

    @Override
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPedido(pedidoId);
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());

        Pedido pedidoActualizado = pedidoRepositoryPort.guardar(pedido);
        notificacionPort.notificarCambioEstado(pedidoActualizado);
        return pedidoActualizado;
    }

    @Override
    public Pedido obtenerPedido(Long pedidoId) {
        return pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));
    }

    @Override
    public List<Pedido> listarPedidos(EstadoPedido estado) {
        return estado == null ? pedidoRepositoryPort.buscarTodos() : pedidoRepositoryPort.buscarPorEstado(estado);
    }

    private void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new PedidoInvalidoException("La mesa debe ser un numero mayor que 0");
        }
    }

    private void validarLineas(List<LineaPedido> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new PedidoInvalidoException("El pedido debe contener al menos un producto");
        }
        for (LineaPedido linea : lineas) {
            if (linea.getCantidad() <= 0) {
                throw new PedidoInvalidoException(
                        "La cantidad de '%s' debe ser mayor que 0".formatted(linea.getNombreProducto()));
            }
        }
    }

    private void validarTransicionEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        if (!TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of()).contains(nuevoEstado)) {
            throw new TransicionEstadoInvalidaException(estadoActual, nuevoEstado);
        }
    }
}
