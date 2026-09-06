package com.tpvgallega.backend.infrastructure.persistence;

import com.tpvgallega.backend.domain.model.TipoProducto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lineas_pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedidoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreProducto;
    private int cantidad;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    private String notas;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private PedidoJpaEntity pedido;
}
