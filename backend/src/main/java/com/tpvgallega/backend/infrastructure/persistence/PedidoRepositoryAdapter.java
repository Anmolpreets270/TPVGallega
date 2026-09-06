package com.tpvgallega.backend.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tpvgallega.backend.application.port.out.PedidoRepositoryPort;
import com.tpvgallega.backend.domain.model.EstadoPedido;
import com.tpvgallega.backend.domain.model.Pedido;
import com.tpvgallega.backend.infrastructure.persistence.mapper.PedidoEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoJpaRepository jpaRepository;
    private final PedidoEntityMapper mapper;

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoJpaEntity entityGuardada = jpaRepository.save(mapper.toEntity(pedido));
        return mapper.toDomain(entityGuardada);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return jpaRepository.findByEstado(estado).stream().map(mapper::toDomain).toList();
    }
}
