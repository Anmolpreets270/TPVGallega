package com.tpvgallega.backend.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private Long id;
    private int mesa;
    private List<LineaPedido> lineas;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public boolean tieneLineasDeTipo(TipoProducto tipo) {
        return lineas != null && lineas.stream().anyMatch(linea -> linea.getTipoProducto() == tipo);
    }
}
