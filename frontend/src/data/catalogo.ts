import { TipoProducto } from '../types/pedido';

export type Categoria = 'Entrantes' | 'Principales' | 'Bebidas';

export interface ProductoCatalogo {
  nombre: string;
  categoria: Categoria;
  tipoProducto: TipoProducto;
  descripcion?: string;
}

export const CATEGORIAS_ORDEN: Categoria[] = ['Entrantes', 'Principales', 'Bebidas'];

export const CATALOGO: ProductoCatalogo[] = [
  // Entrantes - 9€ con bebida y pan
  {
    nombre: 'Ensalada basica de atun',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Lechuga, tomate, pepino, zanahoria y huevo duro',
  },
  {
    nombre: 'Huevos rotos con lacon',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas caseras',
  },
  {
    nombre: 'Huevos rotos con jamon',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas caseras',
  },
  {
    nombre: 'Huevos rotos con beicon',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas caseras',
  },
  {
    nombre: 'Macarrones a la bolonesa',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con toque parmesano',
  },
  {
    nombre: 'Macarrones frutti di mare',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con toque parmesano',
  },
  {
    nombre: 'Macarrones napolitana',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con toque parmesano',
  },
  {
    nombre: 'Croquetas (3 unds.)',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con fritas y huevo frito',
  },
  {
    nombre: 'Ensalada de tomate y atun',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con aliño de ajo y perejil',
  },
  {
    nombre: 'Almejas a la marinera',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con toque de ajo y perejil',
  },
  {
    nombre: 'Mejillones a la marinera',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con toque de ajo y perejil',
  },
  {
    nombre: 'Guiso de sepia con patatas',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Casero',
  },
  {
    nombre: 'Raxo gallego',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Dados de lomo con pimientos y patatas, pan y huevo frito',
  },
  {
    nombre: 'Ensalada de pollo Kentucky',
    categoria: 'Entrantes',
    tipoProducto: 'COMIDA',
    descripcion: 'Con alioli ligero casero',
  },

  // Principales - 14€ con bebida y pan
  {
    nombre: 'Codillo al horno',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas, huevo frito, pan y pimiento',
  },
  {
    nombre: 'Fingers de pollo',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con huevo frito y patatas fritas',
  },
  {
    nombre: 'Paella de mariscos',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Preparada al momento',
  },
  {
    nombre: 'Paella de verduras y sepia',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Preparada al momento',
  },
  {
    nombre: 'Arroz caldoso a la marinera',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Preparado al momento',
  },
  {
    nombre: 'Butifarra de pages al horno',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas y huevo frito',
  },
  {
    nombre: 'Lomo a la brasa',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas y huevo frito',
  },
  {
    nombre: 'Cazuela de mariscos',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Gambas a la marinera con almejas y mejillones',
  },
  {
    nombre: 'Chocos rebozados',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas, huevo y pan',
  },
  {
    nombre: 'Tiras de sepia a la plancha',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con ensalada',
  },
  {
    nombre: 'Entrecot de carne gallega',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'A la brasa, con guarnicion a elegir y alioli casero',
  },
  {
    nombre: 'Escalopa de pollo',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas, huevo frito y pimiento frito',
  },
  {
    nombre: 'Escalopa de cerdo',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas, huevo frito y pimiento frito',
  },
  {
    nombre: 'Mariscada',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Gambas, cigalas, navajas, sepia a la plancha, mejillones y almejas',
  },
  {
    nombre: 'Gambas a la plancha',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con guarnicion a elegir',
  },
  {
    nombre: 'Hamburguesa de pollo Kentucky',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con beicon, huevo, queso y lechuga, con fritas',
  },
  {
    nombre: 'Secreto a la brasa',
    categoria: 'Principales',
    tipoProducto: 'COMIDA',
    descripcion: 'Con patatas fritas, huevo frito y pimiento',
  },

  // Bebidas
  { nombre: 'Cana de cerveza', categoria: 'Bebidas', tipoProducto: 'BEBIDA' },
  { nombre: 'Vino Ribeiro', categoria: 'Bebidas', tipoProducto: 'BEBIDA' },
  { nombre: 'Agua con gas', categoria: 'Bebidas', tipoProducto: 'BEBIDA' },
  { nombre: 'Cafe', categoria: 'Bebidas', tipoProducto: 'BEBIDA' },
];
