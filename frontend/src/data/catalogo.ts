import { TipoProducto } from '../types/pedido';

export interface ProductoCatalogo {
  nombre: string;
  tipoProducto: TipoProducto;
}

export const CATALOGO: ProductoCatalogo[] = [
  { nombre: 'Pulpo a feira', tipoProducto: 'COMIDA' },
  { nombre: 'Empanada gallega', tipoProducto: 'COMIDA' },
  { nombre: 'Zorza', tipoProducto: 'COMIDA' },
  { nombre: 'Croquetas caseras', tipoProducto: 'COMIDA' },
  { nombre: 'Tarta de Santiago', tipoProducto: 'COMIDA' },
  { nombre: 'Cana de cerveza', tipoProducto: 'BEBIDA' },
  { nombre: 'Vino Ribeiro', tipoProducto: 'BEBIDA' },
  { nombre: 'Agua con gas', tipoProducto: 'BEBIDA' },
  { nombre: 'Cafe', tipoProducto: 'BEBIDA' },
];

export const NUMERO_DE_MESAS = 20;
