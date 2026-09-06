import { Client, IMessage } from '@stomp/stompjs';

import { WS_URL } from './env';

let cliente: Client | null = null;

function obtenerCliente(): Client {
  if (!cliente) {
    cliente = new Client({
      brokerURL: WS_URL,
      reconnectDelay: 3000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
    });
    cliente.activate();
  }
  return cliente;
}

export function suscribirseATopic(topic: string, onMensaje: (mensaje: IMessage) => void): () => void {
  const clienteStomp = obtenerCliente();

  const suscribir = () => clienteStomp.subscribe(topic, onMensaje);

  if (clienteStomp.connected) {
    const suscripcion = suscribir();
    return () => suscripcion.unsubscribe();
  }

  let suscripcionActual: ReturnType<typeof suscribir> | null = null;
  clienteStomp.onConnect = () => {
    suscripcionActual = suscribir();
  };

  return () => suscripcionActual?.unsubscribe();
}
