package com.tpvgallega.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {

    private Long id;
    private String nombreProducto;
    private int cantidad;
    private TipoProducto tipoProducto;
    private String notas;
}
