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

### Base de datos

El backend necesita PostgreSQL corriendo en `localhost:5432`. El repo incluye un
`docker-compose.yml` con los mismos valores que usa el backend por defecto
(base `tpvgallega`, usuario/contrasena `postgres`), asi que no hace falta
configurar nada mas:

```bash
docker compose up -d
```

La primera vez que arranca (con el volumen de datos vacio), Postgres ejecuta
automaticamente `db/init.sql`, que crea las tablas `pedidos` y
`lineas_pedido`. Si ya tenias el contenedor levantado de antes (creado sin
este script), tienes dos opciones:

- No hacer nada: Hibernate (`ddl-auto: update`) ya habia creado esas tablas
  al arrancar el backend, asi que todo sigue funcionando igual.
- O recrear el volumen para que el script se ejecute desde cero (**esto
  borra los pedidos guardados hasta ahora**): `docker compose down -v && docker compose up -d`.

### Ejecutar

No hace falta tener Maven instalado: el proyecto incluye el Maven Wrapper.

```bash
cd backend
./mvnw spring-boot:run        # Linux/Mac
.\mvnw.cmd spring-boot:run     # Windows (PowerShell/cmd)
```

(Si prefieres usar tu propio Maven: `mvn spring-boot:run`)

Variables de entorno (con valores por defecto para desarrollo local, ya
coherentes con el `docker-compose.yml`):
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
./mvnw test
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
src/data/       Menu del restaurante (catalogo.ts) y zonas/mesas (mesas.ts)
src/theme/      Paleta de colores compartida por toda la app
src/screens/    Pantallas de camarero (mesas, nueva comanda) y de sala (cocina/barra)
src/navigation/ Navegador principal, que decide el flujo segun el rol
```

### Zonas y mesas

Definidas en `src/data/mesas.ts`: **Sala** (mesas 1-20) y **Terraza** (mesas
41-48 y 51-55). La pantalla de mesas del camarero permite alternar entre
zonas y resalta las mesas con una comanda activa.

### Menu

El catalogo de platos y bebidas (`src/data/catalogo.ts`) esta organizado por
categoria (Entrantes, Principales, Bebidas) y se puede filtrar por texto al
crear una comanda.
