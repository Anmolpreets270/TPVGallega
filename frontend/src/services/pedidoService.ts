import { apiClient } from '../config/api';
import { CrearPedidoRequest, EstadoPedido, Pedido } from '../types/pedido';

export async function crearPedido(request: CrearPedidoRequest): Promise<Pedido> {
  const { data } = await apiClient.post<Pedido>('/pedidos', request);
  return data;
}

export async function listarPedidos(estado?: EstadoPedido): Promise<Pedido[]> {
  const { data } = await apiClient.get<Pedido[]>('/pedidos', { params: estado ? { estado } : undefined });
  return data;
}

export async function cambiarEstadoPedido(id: number, estado: EstadoPedido): Promise<Pedido> {
  const { data } = await apiClient.patch<Pedido>(`/pedidos/${id}/estado`, { estado });
  return data;
}
