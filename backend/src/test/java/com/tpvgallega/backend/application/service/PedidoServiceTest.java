package com.tpvgallega.backend.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.tpvgallega.backend.application.port.out.NotificacionPort;
import com.tpvgallega.backend.application.port.out.PedidoRepositoryPort;
import com.tpvgallega.backend.domain.exception.PedidoInvalidoException;
import com.tpvgallega.backend.domain.exception.PedidoNoEncontradoException;
import com.tpvgallega.backend.domain.exception.TransicionEstadoInvalidaException;
import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.domain.model.TipoProducto;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @Mock
    private NotificacionPort notificacionPort;

    @InjectMocks
    private PedidoService pedidoService;

    private final Answer<Pedido> devolverElMismoPedido = (InvocationOnMock invocation) -> invocation.getArgument(0);

    private LineaPedido lineaValida() {
        return LineaPedido.builder()
                .nombreProducto("Pulpo a feira")
                .cantidad(2)
                .tipoProducto(TipoProducto.COMIDA)
                .build();
    }

    private Pedido pedidoValido() {
        return Pedido.builder()
                .mesa(4)
                .lineas(List.of(lineaValida()))
                .build();
    }

    @Test
    void crearPedidoConMesaCeroLanzaExcepcion() {
        Pedido pedido = Pedido.builder().mesa(0).lineas(List.of(lineaValida())).build();

        assertThatThrownBy(() -> pedidoService.crearPedido(pedido))
                .isInstanceOf(PedidoInvalidoException.class)
                .hasMessageContaining("mesa");
        verifyNoInteractions(notificacionPort);
    }

    @Test
    void crearPedidoSinLineasLanzaExcepcion() {
        Pedido pedido = Pedido.builder().mesa(4).lineas(List.of()).build();

        assertThatThrownBy(() -> pedidoService.crearPedido(pedido))
                .isInstanceOf(PedidoInvalidoException.class)
                .hasMessageContaining("al menos un producto");
    }

    @Test
    void crearPedidoConCantidadCeroLanzaExcepcion() {
        LineaPedido lineaInvalida = LineaPedido.builder()
                .nombreProducto("Empanada")
                .cantidad(0)
                .tipoProducto(TipoProducto.COMIDA)
                .build();
        Pedido pedido = Pedido.builder().mesa(4).lineas(List.of(lineaInvalida)).build();

        assertThatThrownBy(() -> pedidoService.crearPedido(pedido))
                .isInstanceOf(PedidoInvalidoException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void crearPedidoValidoQuedaPendienteYNotifica() {
        Pedido pedido = pedidoValido();
        when(pedidoRepositoryPort.guardar(any(Pedido.class))).thenAnswer(devolverElMismoPedido);

        Pedido resultado = pedidoService.crearPedido(pedido);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.PENDIENTE);
        assertThat(resultado.getFechaCreacion()).isNotNull();
        verify(notificacionPort).notificarNuevoPedido(resultado);
    }

    @Test
    void cambiarEstadoConTransicionValidaFunciona() {
        Pedido pedidoExistente = pedidoValido().toBuilder().id(1L).estado(EstadoPedido.PENDIENTE).build();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepositoryPort.guardar(any(Pedido.class))).thenAnswer(devolverElMismoPedido);

        Pedido resultado = pedidoService.cambiarEstado(1L, EstadoPedido.PREPARANDO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.PREPARANDO);
        verify(notificacionPort).notificarCambioEstado(resultado);
    }

    @Test
    void cambiarEstadoConTransicionInvalidaLanzaExcepcion() {
        Pedido pedidoExistente = pedidoValido().toBuilder().id(1L).estado(EstadoPedido.PENDIENTE).build();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedidoExistente));

        assertThatThrownBy(() -> pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO))
                .isInstanceOf(TransicionEstadoInvalidaException.class);
    }

    @Test
    void cambiarEstadoDePedidoInexistenteLanzaExcepcion() {
        when(pedidoRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.cambiarEstado(99L, EstadoPedido.PREPARANDO))
                .isInstanceOf(PedidoNoEncontradoException.class);
    }

    @Test
    void noSePuedeCambiarEstadoDePedidoEntregado() {
        Pedido pedidoExistente = pedidoValido().toBuilder().id(1L).estado(EstadoPedido.ENTREGADO).build();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedidoExistente));

        assertThatThrownBy(() -> pedidoService.cambiarEstado(1L, EstadoPedido.CANCELADO))
                .isInstanceOf(TransicionEstadoInvalidaException.class);
    }
}
