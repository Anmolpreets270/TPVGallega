# TPVGallega

Sistema integral de gestion para restaurantes que conecta la sala (camareros) con las
zonas de preparacion (cocina y barra) en tiempo real. Cuando un camarero envia una
comanda desde su movil, el ticket aparece al instante en las pantallas de cocina y/o
barra via WebSocket (STOMP), sin necesidad de recargar.

## Estructura del repositorio

```
backend/    Spring Boot (Java 21) - arquitectura hexagonal
frontend/   React Native + TypeScript (Expo) - app de camareros y pantallas de sala
```

## Backend

Arquitectura hexagonal con tres capas:

- **domain**: `Pedido`, `LineaPedido`, `EstadoPedido`, `TipoProducto` y las excepciones
  de negocio. Sin dependencias de Spring.
- **application**: puerto de entrada `GestionarPedidoUseCase`, puertos de salida
  `PedidoRepositoryPort` y `NotificacionPort`, y el servicio `PedidoService` con las
  validaciones (mesa > 0, pedido con productos, cantidades > 0, transiciones de
  estado validas).
- **infrastructure**: adaptador REST (`PedidoController`), adaptador de persistencia
  JPA (PostgreSQL) y adaptador WebSocket (STOMP) que publica en `/topic/cocina` y
  `/topic/barra` segun el tipo de producto de cada linea del pedido.

### Ejecutar

```bash
cd backend
mvn spring-boot:run
```

Variables de entorno (con valores por defecto para desarrollo local):
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `SERVER_PORT`.

### API REST

| Metodo | Ruta                     | Descripcion                          |
|--------|--------------------------|---------------------------------------|
| POST   | `/api/pedidos`           | Crea un pedido (queda en `PENDIENTE`) |
| GET    | `/api/pedidos`           | Lista pedidos (filtro opcional `?estado=`) |
| GET    | `/api/pedidos/{id}`      | Obtiene un pedido                     |
| PATCH  | `/api/pedidos/{id}/estado` | Cambia el estado del pedido         |

Ciclo de vida: `PENDIENTE -> PREPARANDO -> LISTO -> ENTREGADO`, con `CANCELADO`
posible desde cualquier estado no terminal.

### WebSocket

Endpoint STOMP en `/ws`. Canales: `/topic/cocina` y `/topic/barra`.

### Tests

```bash
cd backend
mvn test
```

## Frontend

App unica en React Native + TypeScript (Expo), que se comporta como app de
camareros o como pantalla de sala segun la variable `EXPO_PUBLIC_APP_ROLE`:

- `camarero` (por defecto): selector de mesas y creacion de comandas.
- `cocina` / `barra`: pantalla de visualizacion que escucha el topic
  correspondiente y permite avanzar el estado del ticket.

### Ejecutar

```bash
cd frontend
npm install
EXPO_PUBLIC_API_URL=http://localhost:8080 EXPO_PUBLIC_APP_ROLE=camarero npx expo start
```

Para las pantallas de cocina/barra (tablet o `--web`):

```bash
EXPO_PUBLIC_API_URL=http://<ip-servidor>:8080 EXPO_PUBLIC_APP_ROLE=cocina npx expo start --web
```

### Estructura

```
src/config/     API REST, WebSocket STOMP y variables de entorno
src/types/      Tipos que reflejan los DTOs del backend
src/services/   Llamadas REST (pedidoService)
src/data/       Catalogo de productos de ejemplo
src/screens/    Pantallas de camarero (mesas, nueva comanda) y de sala (cocina/barra)
src/navigation/ Navegador principal, que decide el flujo segun el rol
```
