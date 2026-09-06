export interface ZonaMesas {
  id: string;
  nombre: string;
  mesas: number[];
}

function rango(desde: number, hasta: number): number[] {
  return Array.from({ length: hasta - desde + 1 }, (_, indice) => desde + indice);
}

export const ZONAS_MESAS: ZonaMesas[] = [
  { id: 'sala', nombre: 'Sala', mesas: rango(1, 20) },
  { id: 'terraza', nombre: 'Terraza', mesas: [...rango(41, 48), ...rango(51, 55)] },
];
