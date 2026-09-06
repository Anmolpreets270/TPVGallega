import { useFocusEffect } from '@react-navigation/native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import React, { useCallback, useState } from 'react';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';

import { ZONAS_MESAS } from '../../data/mesas';
import { CamareroStackParamList } from '../../navigation/AppNavigator';
import { listarPedidos } from '../../services/pedidoService';
import { colors } from '../../theme/colors';

type Props = NativeStackScreenProps<CamareroStackParamList, 'Mesas'>;

export function MesasScreen({ navigation }: Props) {
  const [zonaActiva, setZonaActiva] = useState(ZONAS_MESAS[0]);
  const [mesasConComandaActiva, setMesasConComandaActiva] = useState<Set<number>>(new Set());

  useFocusEffect(
    useCallback(() => {
      listarPedidos()
        .then((pedidos) => {
          const activos = pedidos.filter((p) => p.estado === 'PENDIENTE' || p.estado === 'PREPARANDO' || p.estado === 'LISTO');
          setMesasConComandaActiva(new Set(activos.map((p) => p.mesa)));
        })
        .catch(() => undefined);
    }, []),
  );

  return (
    <View style={styles.contenedor}>
      <View style={styles.selectorZonas}>
        {ZONAS_MESAS.map((zona) => {
          const activa = zona.id === zonaActiva.id;
          return (
            <Pressable
              key={zona.id}
              style={[styles.chipZona, activa && styles.chipZonaActiva]}
              onPress={() => setZonaActiva(zona)}
            >
              <Text style={[styles.chipZonaTexto, activa && styles.chipZonaTextoActivo]}>{zona.nombre}</Text>
            </Pressable>
          );
        })}
      </View>
      <FlatList
        data={zonaActiva.mesas}
        numColumns={4}
        key={zonaActiva.id}
        contentContainerStyle={styles.grid}
        keyExtractor={(mesa) => String(mesa)}
        renderItem={({ item: mesa }) => {
          const ocupada = mesasConComandaActiva.has(mesa);
          return (
            <Pressable
              style={[styles.mesa, ocupada && styles.mesaOcupada]}
              onPress={() => navigation.navigate('NuevoPedido', { mesa })}
            >
              <Text style={styles.mesaTexto}>{mesa}</Text>
              {ocupada && <View style={styles.indicadorOcupada} />}
            </Pressable>
          );
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    padding: 16,
    backgroundColor: colors.background,
  },
  selectorZonas: {
    flexDirection: 'row',
    marginBottom: 16,
    gap: 8,
  },
  chipZona: {
    paddingVertical: 8,
    paddingHorizontal: 18,
    borderRadius: 20,
    backgroundColor: colors.surface,
    borderWidth: 1,
    borderColor: colors.border,
  },
  chipZonaActiva: {
    backgroundColor: colors.primary,
    borderColor: colors.primary,
  },
  chipZonaTexto: {
    fontSize: 15,
    fontWeight: '600',
    color: colors.textSecondary,
  },
  chipZonaTextoActivo: {
    color: colors.textOnPrimary,
  },
  grid: {
    paddingBottom: 16,
  },
  mesa: {
    flexBasis: '23%',
    flexGrow: 0,
    aspectRatio: 1,
    margin: '1%',
    borderRadius: 14,
    backgroundColor: colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  mesaOcupada: {
    backgroundColor: colors.primaryDark,
  },
  mesaTexto: {
    color: colors.textOnPrimary,
    fontSize: 24,
    fontWeight: 'bold',
  },
  indicadorOcupada: {
    position: 'absolute',
    top: 8,
    right: 8,
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: colors.accent,
  },
});
