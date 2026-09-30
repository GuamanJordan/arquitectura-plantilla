# Arquitectura - Plantilla

Plantilla academica para la asignatura de Arquitectura de Software. El repositorio permite levantar una computadora como servidor o como cliente usando Java, Jakarta EE, .NET, REST, SOAP, gRPC, bases de datos y BPM.

## Inicio rapido con Docker

```bash
cp .env.example .env
chmod +x scripts/*.sh
./scripts/verificar-entorno.sh
./scripts/levantar-infra.sh
./scripts/inicializar-sqlserver.sh
```

Servicios principales:

| Servicio | Puerto local |
| --- | --- |
| GlassFish App | 8082 |
| GlassFish Admin | 4850 |
| Payara App | 8081 |
| Payara Admin | 4849 |
| .NET REST | 5100 |
| .NET gRPC | 50051 |
| MariaDB | 3307 |
| MySQL | 3308 |
| SQL Server | 1433 |
| Zeebe/Camunda | 26500 |

## Uso en red local

En la computadora servidor:

```bash
hostname -I
docker compose up -d
```

En la computadora cliente:

```bash
cp .env.example .env
./scripts/configurar-cliente.sh 192.168.1.50
./scripts/probar-red.sh 192.168.1.50
./scripts/levantar-clientes.sh 192.168.1.50
```

Luego ejecutar clientes usando esa IP. El proceso completo esta en `docs/12-modo-cliente-servidor.md`.

## Modulos

- `servidores/jakarta-rest-glassfish`: CRUD REST de productos.
- `servidores/jakarta-soap-glassfish`: operaciones SOAP de productos.
- `servidores/jakarta-mvc-payara`: flujo MVC de login, dashboard y productos.
- `servidores/dotnet-rest-api`: CRUD REST en .NET.
- `servidores/dotnet-grpc-api`: servicio gRPC de productos.
- `clientes/consola-java`: cliente REST con `HttpClient`.
- `clientes/consola-dotnet`: cliente REST y gRPC.
- `clientes/escritorio-java`: cliente Swing.
- `clientes/web-java`: cliente web Java.
- `clientes/web-dotnet`: cliente web .NET.
- `clientes/movil-android-java`: cliente Android Java.
- `clientes/movil-dotnet-maui`: cliente Android .NET MAUI.

## Instalacion directa

La ruta sin Docker esta documentada en `docs/02-instalacion-debian.md`, `docs/03-instalacion-ubuntu.md` y `docs/05-servidores.md`. La plantilla no versiona SDKs, ZIPs de servidores, binarios ni bases de datos fisicas.
