import React, { useCallback, useEffect, useState } from 'react';
import { FlatList, Pressable, StyleSheet, Text, Vibration, View } from 'react-native';

import { TicketCard } from '../../components/TicketCard';
import { suscribirseATopic } from '../../config/websocket';
import { cambiarEstadoPedido, listarPedidos } from '../../services/pedidoService';
import { EstadoPedido, Pedido } from '../../types/pedido';
import { colors } from '../../theme/colors';

interface Props {
  zona: 'cocina' | 'barra';
}

const SIGUIENTE_ESTADO: Partial<Record<EstadoPedido, EstadoPedido>> = {
  PENDIENTE: 'PREPARANDO',
  PREPARANDO: 'LISTO',
};

export function PantallaZonaScreen({ zona }: Props) {
  const [pedidos, setPedidos] = useState<Pedido[]>([]);

  const actualizarPedido = useCallback((pedidoActualizado: Pedido) => {
    setPedidos((actual) => {
      const yaExiste = actual.some((pedido) => pedido.id === pedidoActualizado.id);
      if (pedidoActualizado.estado === 'ENTREGADO' || pedidoActualizado.estado === 'CANCELADO') {
        return actual.filter((pedido) => pedido.id !== pedidoActualizado.id);
      }
      if (!yaExiste) {
        Vibration.vibrate(400);
        return [...actual, pedidoActualizado];
      }
      return actual.map((pedido) => (pedido.id === pedidoActualizado.id ? pedidoActualizado : pedido));
    });
  }, []);

  useEffect(() => {
    listarPedidos()
      .then((pedidosIniciales) =>
        setPedidos(pedidosIniciales.filter((p) => p.estado === 'PENDIENTE' || p.estado === 'PREPARANDO')),
      )
      .catch(() => undefined);

    return suscribirseATopic(`/topic/${zona}`, (mensaje) => {
      actualizarPedido(JSON.parse(mensaje.body) as Pedido);
    });
  }, [zona, actualizarPedido]);

  const avanzarEstado = async (pedido: Pedido) => {
    const nuevoEstado = SIGUIENTE_ESTADO[pedido.estado];
    if (!nuevoEstado) {
      return;
    }
    try {
      const pedidoActualizado = await cambiarEstadoPedido(pedido.id, nuevoEstado);
      actualizarPedido(pedidoActualizado);
    } catch {
      // El ticket se refrescara con la proxima notificacion o recarga
    }
  };

  return (
    <View style={styles.contenedor}>
      <View style={styles.cabecera}>
        <Text style={styles.titulo}>{zona === 'cocina' ? 'Cocina' : 'Barra'}</Text>
        <View style={styles.contador}>
          <Text style={styles.contadorTexto}>{pedidos.length}</Text>
        </View>
      </View>
      {pedidos.length === 0 ? (
        <Text style={styles.sinTickets}>No hay comandas pendientes</Text>
      ) : (
        <FlatList
          data={pedidos}
          horizontal
          keyExtractor={(pedido) => String(pedido.id)}
          renderItem={({ item: pedido }) => (
            <TicketCard pedido={pedido}>
              {SIGUIENTE_ESTADO[pedido.estado] && (
                <Pressable style={styles.botonAvanzar} onPress={() => avanzarEstado(pedido)}>
                  <Text style={styles.botonAvanzarTexto}>Marcar {SIGUIENTE_ESTADO[pedido.estado]}</Text>
                </Pressable>
              )}
            </TicketCard>
          )}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    padding: 16,
    backgroundColor: colors.background,
  },
  cabecera: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  titulo: {
    fontSize: 24,
    fontWeight: 'bold',
    color: colors.textPrimary,
  },
  contador: {
    marginLeft: 10,
    backgroundColor: colors.accent,
    borderRadius: 12,
    paddingHorizontal: 10,
    paddingVertical: 2,
  },
  contadorTexto: {
    color: colors.textOnPrimary,
    fontWeight: '700',
  },
  sinTickets: {
    color: colors.textSecondary,
    fontSize: 16,
  },
  botonAvanzar: {
    marginTop: 8,
    backgroundColor: colors.primary,
    padding: 8,
    borderRadius: 6,
    alignItems: 'center',
  },
  botonAvanzarTexto: {
    color: colors.textOnPrimary,
    fontWeight: '600',
  },
});
