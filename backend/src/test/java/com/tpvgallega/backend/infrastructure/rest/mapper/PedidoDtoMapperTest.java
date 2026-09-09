package com.tpvgallega.backend.infrastructure.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.domain.model.TipoProducto;
import com.tpvgallega.backend.infrastructure.rest.dto.CrearPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.LineaPedidoRequest;
import com.tpvgallega.backend.infrastructure.rest.dto.PedidoResponse;

class PedidoDtoMapperTest {

    private final PedidoDtoMapper mapper = new PedidoDtoMapperImpl();

    @Test
    void toDomainDejaSinAsignarLosCamposQueDecideElServicio() {
        CrearPedidoRequest request = new CrearPedidoRequest(
                4, List.of(new LineaPedidoRequest("Pulpo a feira", 2, TipoProducto.COMIDA, "sin sal")));

        Pedido pedido = mapper.toDomain(request);

        assertThat(pedido.getMesa()).isEqualTo(4);
        assertThat(pedido.getLineas()).hasSize(1);
        assertThat(pedido.getLineas().get(0).getNombreProducto()).isEqualTo("Pulpo a feira");
        assertThat(pedido.getLineas().get(0).getNotas()).isEqualTo("sin sal");
        assertThat(pedido.getId()).isNull();
        assertThat(pedido.getEstado()).isNull();
        assertThat(pedido.getFechaCreacion()).isNull();
    }

    @Test
    void toResponseMapeaTodosLosCampos() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .mesa(4)
                .estado(EstadoPedido.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .lineas(List.of(LineaPedido.builder()
                        .id(10L)
                        .nombreProducto("Croquetas")
                        .cantidad(1)
                        .tipoProducto(TipoProducto.COMIDA)
                        .build()))
                .build();

        PedidoResponse response = mapper.toResponse(pedido);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.mesa()).isEqualTo(4);
        assertThat(response.estado()).isEqualTo(EstadoPedido.PENDIENTE);
        assertThat(response.lineas()).hasSize(1);
        assertThat(response.lineas().get(0).id()).isEqualTo(10L);
        assertThat(response.lineas().get(0).nombreProducto()).isEqualTo("Croquetas");
    }
}
