package com.tpvgallega.backend.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tpvgallega.backend.domain.model.EstadoPedido;

public interface SpringDataPedidoJpaRepository extends JpaRepository<PedidoJpaEntity, Long> {

    List<PedidoJpaEntity> findByEstado(EstadoPedido estado);
}
