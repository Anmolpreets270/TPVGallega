import { NativeStackScreenProps } from '@react-navigation/native-stack';
import React, { useMemo, useState } from 'react';
import { Alert, Pressable, SectionList, StyleSheet, Text, TextInput, View } from 'react-native';

import { CATALOGO, CATEGORIAS_ORDEN, ProductoCatalogo } from '../../data/catalogo';
import { CamareroStackParamList } from '../../navigation/AppNavigator';
import { crearPedido } from '../../services/pedidoService';
import { LineaPedido } from '../../types/pedido';
import { colors } from '../../theme/colors';

type Props = NativeStackScreenProps<CamareroStackParamList, 'NuevoPedido'>;

export function NuevoPedidoScreen({ route, navigation }: Props) {
  const { mesa } = route.params;
  const [busqueda, setBusqueda] = useState('');
  const [cantidades, setCantidades] = useState<Record<string, number>>({});
  const [enviando, setEnviando] = useState(false);

  const secciones = useMemo(() => {
    const termino = busqueda.trim().toLowerCase();
    const filtrado = termino ? CATALOGO.filter((p) => p.nombre.toLowerCase().includes(termino)) : CATALOGO;

    return CATEGORIAS_ORDEN.map((categoria) => ({
      title: categoria,
      data: filtrado.filter((producto) => producto.categoria === categoria),
    })).filter((seccion) => seccion.data.length > 0);
  }, [busqueda]);

  const totalUnidades = useMemo(
    () => Object.values(cantidades).reduce((suma, cantidad) => suma + cantidad, 0),
    [cantidades],
  );

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
      <TextInput
        style={styles.buscador}
        placeholder="Buscar plato o bebida..."
        placeholderTextColor={colors.textSecondary}
        value={busqueda}
        onChangeText={setBusqueda}
      />
      <SectionList
        sections={secciones}
        keyExtractor={(producto) => producto.nombre}
        stickySectionHeadersEnabled
        renderSectionHeader={({ section }) => (
          <View style={styles.cabeceraSeccion}>
            <Text style={styles.tituloSeccion}>{section.title}</Text>
          </View>
        )}
        renderItem={({ item: producto }: { item: ProductoCatalogo }) => (
          <View style={styles.fila}>
            <View style={styles.infoProducto}>
              <Text style={styles.nombreProducto}>{producto.nombre}</Text>
              {producto.descripcion && <Text style={styles.descripcionProducto}>{producto.descripcion}</Text>}
            </View>
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
        ListEmptyComponent={<Text style={styles.sinResultados}>No hay platos que coincidan con la busqueda</Text>}
      />
      <Pressable
        style={[styles.botonEnviar, totalUnidades === 0 && styles.botonEnviarDeshabilitado]}
        onPress={enviarComanda}
        disabled={enviando || totalUnidades === 0}
      >
        <Text style={styles.botonEnviarTexto}>
          {enviando ? 'Enviando...' : `Enviar comanda${totalUnidades > 0 ? ` (${totalUnidades})` : ''}`}
        </Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    padding: 16,
    backgroundColor: colors.background,
  },
  buscador: {
    backgroundColor: colors.surface,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
    fontSize: 15,
    color: colors.textPrimary,
    marginBottom: 12,
  },
  cabeceraSeccion: {
    backgroundColor: colors.background,
    paddingTop: 8,
    paddingBottom: 4,
  },
  tituloSeccion: {
    fontSize: 13,
    fontWeight: '700',
    color: colors.primary,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  fila: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: colors.surface,
    borderRadius: 10,
    paddingVertical: 10,
    paddingHorizontal: 12,
    marginBottom: 8,
  },
  infoProducto: {
    flex: 1,
    paddingRight: 12,
  },
  nombreProducto: {
    fontSize: 16,
    color: colors.textPrimary,
    fontWeight: '500',
  },
  descripcionProducto: {
    fontSize: 12,
    color: colors.textSecondary,
    marginTop: 2,
  },
  selectorCantidad: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  boton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  botonTexto: {
    color: colors.textOnPrimary,
    fontSize: 18,
    fontWeight: 'bold',
  },
  cantidad: {
    width: 32,
    textAlign: 'center',
    fontSize: 16,
    color: colors.textPrimary,
  },
  sinResultados: {
    textAlign: 'center',
    color: colors.textSecondary,
    marginTop: 24,
  },
  botonEnviar: {
    marginTop: 12,
    backgroundColor: colors.accent,
    padding: 16,
    borderRadius: 10,
    alignItems: 'center',
  },
  botonEnviarDeshabilitado: {
    backgroundColor: colors.neutral,
  },
  botonEnviarTexto: {
    color: colors.textOnPrimary,
    fontSize: 18,
    fontWeight: 'bold',
  },
});
