export type AppRole = 'camarero' | 'cocina' | 'barra';

const ROLES_VALIDOS: AppRole[] = ['camarero', 'cocina', 'barra'];

function leerRol(): AppRole {
  const valor = process.env.EXPO_PUBLIC_APP_ROLE as AppRole | undefined;
  return valor && ROLES_VALIDOS.includes(valor) ? valor : 'camarero';
}

export const APP_ROLE: AppRole = leerRol();

export const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL ?? 'http://localhost:8080';

export const WS_URL = process.env.EXPO_PUBLIC_WS_URL ?? `${API_BASE_URL.replace(/^http/, 'ws')}/ws`;
