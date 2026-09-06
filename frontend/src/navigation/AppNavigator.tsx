import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import React from 'react';

import { APP_ROLE } from '../config/env';
import { MesasScreen } from '../screens/camarero/MesasScreen';
import { NuevoPedidoScreen } from '../screens/camarero/NuevoPedidoScreen';
import { PantallaZonaScreen } from '../screens/pantalla/PantallaZonaScreen';

export type CamareroStackParamList = {
  Mesas: undefined;
  NuevoPedido: { mesa: number };
};

const CamareroStack = createNativeStackNavigator<CamareroStackParamList>();

function CamareroNavigator() {
  return (
    <CamareroStack.Navigator>
      <CamareroStack.Screen name="Mesas" component={MesasScreen} options={{ title: 'TPVGallega' }} />
      <CamareroStack.Screen
        name="NuevoPedido"
        component={NuevoPedidoScreen}
        options={({ route }) => ({ title: `Mesa ${route.params.mesa}` })}
      />
    </CamareroStack.Navigator>
  );
}

export function AppNavigator() {
  return (
    <NavigationContainer>
      {APP_ROLE === 'camarero' ? <CamareroNavigator /> : <PantallaZonaScreen zona={APP_ROLE} />}
    </NavigationContainer>
  );
}
