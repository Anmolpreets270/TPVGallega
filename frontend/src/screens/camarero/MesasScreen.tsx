import { NativeStackScreenProps } from '@react-navigation/native-stack';
import React from 'react';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';

import { NUMERO_DE_MESAS } from '../../data/catalogo';
import { CamareroStackParamList } from '../../navigation/AppNavigator';

type Props = NativeStackScreenProps<CamareroStackParamList, 'Mesas'>;

const MESAS = Array.from({ length: NUMERO_DE_MESAS }, (_, indice) => indice + 1);

export function MesasScreen({ navigation }: Props) {
  return (
    <View style={styles.contenedor}>
      <FlatList
        data={MESAS}
        numColumns={4}
        keyExtractor={(mesa) => String(mesa)}
        renderItem={({ item: mesa }) => (
          <Pressable style={styles.mesa} onPress={() => navigation.navigate('NuevoPedido', { mesa })}>
            <Text style={styles.mesaTexto}>{mesa}</Text>
          </Pressable>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    padding: 12,
    backgroundColor: '#f5f5f5',
  },
  mesa: {
    flex: 1,
    aspectRatio: 1,
    margin: 6,
    borderRadius: 12,
    backgroundColor: '#2c3e50',
    alignItems: 'center',
    justifyContent: 'center',
  },
  mesaTexto: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
});
