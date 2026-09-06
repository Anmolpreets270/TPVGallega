import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { Pedido } from '../types/pedido';
import { colors } from '../theme/colors';

const COLOR_POR_ESTADO: Record<Pedido['estado'], string> = {
  PENDIENTE: colors.danger,
  PREPARANDO: colors.warning,
  LISTO: colors.success,
  ENTREGADO: colors.neutral,
  CANCELADO: colors.neutral,
};

interface Props {
  pedido: Pedido;
  children?: React.ReactNode;
}

export function TicketCard({ pedido, children }: Props) {
  return (
    <View style={[styles.card, { borderColor: COLOR_POR_ESTADO[pedido.estado] }]}>
      <View style={styles.cabecera}>
        <Text style={styles.mesa}>Mesa {pedido.mesa}</Text>
        <Text style={[styles.estado, { color: COLOR_POR_ESTADO[pedido.estado] }]}>{pedido.estado}</Text>
      </View>
      {pedido.lineas.map((linea, indice) => (
        <Text key={indice} style={styles.linea}>
          {linea.cantidad}x {linea.nombreProducto}
          {linea.notas ? ` (${linea.notas})` : ''}
        </Text>
      ))}
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderWidth: 2,
    borderRadius: 10,
    padding: 12,
    margin: 8,
    minWidth: 220,
    backgroundColor: colors.surface,
  },
  cabecera: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  mesa: {
    fontSize: 18,
    fontWeight: 'bold',
    color: colors.textPrimary,
  },
  estado: {
    fontSize: 14,
    fontWeight: '600',
  },
  linea: {
    fontSize: 15,
    marginBottom: 2,
    color: colors.textPrimary,
  },
});
