package com.tpvgallega.backend.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.LineaPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.domain.model.TipoProducto;
import com.tpvgallega.backend.infrastructure.persistence.LineaPedidoJpaEntity;
import com.tpvgallega.backend.infrastructure.persistence.PedidoJpaEntity;

class PedidoEntityMapperTest {

    private final PedidoEntityMapper mapper = new PedidoEntityMapperImpl();

    @Test
    void toEntityEnlazaCadaLineaConSuPedido() {
        Pedido pedido = Pedido.builder()
                .mesa(4)
                .estado(EstadoPedido.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .lineas(List.of(
                        LineaPedido.builder().nombreProducto("Pulpo a feira").cantidad(1).tipoProducto(TipoProducto.COMIDA).build(),
                        LineaPedido.builder().nombreProducto("Cana").cantidad(2).tipoProducto(TipoProducto.BEBIDA).build()))
                .build();

        PedidoJpaEntity entity = mapper.toEntity(pedido);

        assertThat(entity.getLineas()).hasSize(2);
        assertThat(entity.getLineas()).allSatisfy(linea -> assertThat(linea.getPedido()).isSameAs(entity));
    }

    @Test
    void toDomainMapeaTodosLosCampos() {
        LineaPedidoJpaEntity linea = LineaPedidoJpaEntity.builder()
                .id(10L)
                .nombreProducto("Empanada")
                .cantidad(3)
                .tipoProducto(TipoProducto.COMIDA)
                .notas("sin cebolla")
                .build();
        PedidoJpaEntity entity = PedidoJpaEntity.builder()
                .id(1L)
                .mesa(7)
                .estado(EstadoPedido.LISTO)
                .lineas(List.of(linea))
                .build();

        Pedido pedido = mapper.toDomain(entity);

        assertThat(pedido.getId()).isEqualTo(1L);
        assertThat(pedido.getMesa()).isEqualTo(7);
        assertThat(pedido.getEstado()).isEqualTo(EstadoPedido.LISTO);
        assertThat(pedido.getLineas()).hasSize(1);
        assertThat(pedido.getLineas().get(0).getNombreProducto()).isEqualTo("Empanada");
        assertThat(pedido.getLineas().get(0).getNotas()).isEqualTo("sin cebolla");
    }
}
