import { NativeStackScreenProps } from '@react-navigation/native-stack';
import React, { useState } from 'react';
import { Alert, FlatList, Pressable, StyleSheet, Text, View } from 'react-native';

import { CATALOGO } from '../../data/catalogo';
import { CamareroStackParamList } from '../../navigation/AppNavigator';
import { crearPedido } from '../../services/pedidoService';
import { LineaPedido } from '../../types/pedido';

type Props = NativeStackScreenProps<CamareroStackParamList, 'NuevoPedido'>;

export function NuevoPedidoScreen({ route, navigation }: Props) {
  const { mesa } = route.params;
  const [cantidades, setCantidades] = useState<Record<string, number>>({});
  const [enviando, setEnviando] = useState(false);

  const cambiarCantidad = (nombreProducto: string, delta: number) => {
    setCantidades((actual) => {
      const nuevaCantidad = Math.max(0, (actual[nombreProducto] ?? 0) + delta);
      return { ...actual, [nombreProducto]: nuevaCantidad };
    });
  };

  const enviarComanda = async () => {
    const lineas: LineaPedido[] = CATALOGO.filter((producto) => (cantidades[producto.nombre] ?? 0) > 0).map(
      (producto) => ({
        nombreProducto: producto.nombre,
        cantidad: cantidades[producto.nombre],
        tipoProducto: producto.tipoProducto,
      }),
    );

    if (lineas.length === 0) {
      Alert.alert('Comanda vacia', 'Selecciona al menos un producto');
      return;
    }

    setEnviando(true);
    try {
      await crearPedido({ mesa, lineas });
      navigation.popToTop();
    } catch (error) {
      Alert.alert('Error al enviar', 'No se pudo enviar la comanda. Intentalo de nuevo.');
    } finally {
      setEnviando(false);
    }
  };

  return (
    <View style={styles.contenedor}>
      <Text style={styles.titulo}>Mesa {mesa}</Text>
      <FlatList
        data={CATALOGO}
        keyExtractor={(producto) => producto.nombre}
        renderItem={({ item: producto }) => (
          <View style={styles.fila}>
            <Text style={styles.nombreProducto}>{producto.nombre}</Text>
            <View style={styles.selectorCantidad}>
              <Pressable style={styles.boton} onPress={() => cambiarCantidad(producto.nombre, -1)}>
                <Text style={styles.botonTexto}>-</Text>
              </Pressable>
              <Text style={styles.cantidad}>{cantidades[producto.nombre] ?? 0}</Text>
              <Pressable style={styles.boton} onPress={() => cambiarCantidad(producto.nombre, 1)}>
                <Text style={styles.botonTexto}>+</Text>
              </Pressable>
            </View>
          </View>
        )}
      />
      <Pressable style={styles.botonEnviar} onPress={enviarComanda} disabled={enviando}>
        <Text style={styles.botonEnviarTexto}>{enviando ? 'Enviando...' : 'Enviar comanda'}</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f5f5f5',
  },
  titulo: {
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 12,
  },
  fila: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
  },
  nombreProducto: {
    fontSize: 16,
    flex: 1,
  },
  selectorCantidad: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  boton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: '#2c3e50',
    alignItems: 'center',
    justifyContent: 'center',
  },
  botonTexto: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  cantidad: {
    width: 32,
    textAlign: 'center',
    fontSize: 16,
  },
  botonEnviar: {
    marginTop: 12,
    backgroundColor: '#27ae60',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
  },
  botonEnviarTexto: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
});
