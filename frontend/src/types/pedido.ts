export type EstadoPedido = 'PENDIENTE' | 'PREPARANDO' | 'LISTO' | 'ENTREGADO' | 'CANCELADO';

export type TipoProducto = 'COMIDA' | 'BEBIDA';

export interface LineaPedido {
  id?: number;
  nombreProducto: string;
  cantidad: number;
  tipoProducto: TipoProducto;
  notas?: string;
}

export interface Pedido {
  id: number;
  mesa: number;
  estado: EstadoPedido;
  fechaCreacion: string;
  fechaActualizacion: string;
  lineas: LineaPedido[];
}

export interface CrearPedidoRequest {
  mesa: number;
  lineas: LineaPedido[];
}
