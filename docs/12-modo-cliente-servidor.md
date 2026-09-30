# Modo cliente-servidor en dos computadoras

Esta guia sirve cuando una computadora levanta los servicios y otra computadora ejecuta los clientes.

## Contexto probado en laboratorio

En la sesion de referencia se uso esta IP de servidor:

```text
192.168.100.13
```

Si la IP cambia, reemplazar `192.168.100.13` por la nueva IP que entregue:

```bash
hostname -I
```

Servicios que quedaron levantados y probados:

```text
.NET REST:
http://192.168.100.13:5100/api/productos

.NET gRPC:
http://192.168.100.13:50051

GlassFish Jakarta REST:
http://192.168.100.13:8082/jakarta-rest-glassfish/api/productos

Payara aplicacion web MVC:
http://192.168.100.13:8081/jakarta-mvc-payara/login
```

Credenciales de la aplicacion web MVC:

```text
admin / admin
cliente / cliente
```

Puertos minimos que deben permitir conexiones desde otras maquinas:

```text
5100   API REST .NET
50051  gRPC .NET
8082   GlassFish REST/SOAP
8081   Payara aplicacion web MVC
```

Si UFW esta activo en el servidor:

```bash
sudo ufw allow 5100
sudo ufw allow 50051
sudo ufw allow 8082
sudo ufw allow 8081
```

## 1. Computadora servidor

Clonar el repositorio:

```bash
git clone https://github.com/GuamanJordan/arquitectura-plantilla.git
cd arquitectura-plantilla
```

Configurar y levantar servicios:

```bash
cp .env.example .env
chmod +x scripts/*.sh
./scripts/verificar-entorno.sh
./scripts/levantar-infra.sh
./scripts/inicializar-sqlserver.sh
```

Obtener la IP local del servidor:

```bash
hostname -I
```

Ejemplo:

```text
192.168.1.50
```

Probar en el servidor:

```bash
curl http://localhost:5100/api/productos
curl http://localhost:8082/jakarta-rest-glassfish/api/productos
curl -I http://localhost:8081/jakarta-mvc-payara/login
```

Si hay firewall activo, abrir como minimo estos puertos:

```text
8082  GlassFish REST/SOAP
8081  Payara MVC
5100  .NET REST
50051 .NET gRPC
3307  MariaDB
3308  MySQL
1433  SQL Server
```

## 2. Computadora cliente

Clonar el mismo repositorio:

```bash
git clone https://github.com/GuamanJordan/arquitectura-plantilla.git
cd arquitectura-plantilla
```

Configurar la IP del servidor:

```bash
cp .env.example .env
chmod +x scripts/*.sh
./scripts/configurar-cliente.sh 192.168.1.50
```

Cambiar `192.168.1.50` por la IP real de la computadora servidor.

Probar conectividad:

```bash
./scripts/probar-red.sh 192.168.1.50
curl http://192.168.1.50:5100/api/productos
curl http://192.168.1.50:8082/jakarta-rest-glassfish/api/productos
```

Levantar todos los clientes posibles desde esta computadora:

```bash
./scripts/levantar-clientes.sh 192.168.1.50
```

El script ejecuta clientes de consola, levanta el cliente web .NET en segundo plano, intenta abrir Swing si hay entorno grafico y compila los clientes moviles si existen las herramientas necesarias.

Para apagar clientes que quedaron en segundo plano:

```bash
./scripts/levantar-clientes.sh stop
```

## 3. Enviar peticiones REST desde la computadora cliente

Listar productos en .NET REST:

```bash
curl http://192.168.1.50:5100/api/productos
```

Crear producto en .NET REST:

```bash
curl -X POST http://192.168.1.50:5100/api/productos \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Producto remoto","descripcion":"Creado desde otra maquina","precio":12.50,"stock":5}'
```

Listar productos en Jakarta REST:

```bash
curl http://192.168.1.50:8082/jakarta-rest-glassfish/api/productos
```

## 4. Ejecutar clientes contra el servidor remoto

Desde la maquina cliente, con el servidor probado en esta guia:

```bash
cp .env.example .env
./scripts/configurar-cliente.sh 192.168.100.13
./scripts/probar-red.sh 192.168.100.13
./scripts/levantar-clientes.sh 192.168.100.13
```

Cliente consola Java:

```bash
cd clientes/consola-java
mvn clean package
java -jar target/cliente-consola-java-1.0.0.jar --server http://192.168.1.50:8082/jakarta-rest-glassfish
```

Cliente consola .NET por REST:

```bash
cd clientes/consola-dotnet
dotnet run -- --server http://192.168.1.50:5100
```

Cliente consola .NET por gRPC:

```bash
cd clientes/consola-dotnet
dotnet run -- --grpc http://192.168.1.50:50051 --grpc-listar
```

Cliente escritorio Java:

```bash
cd clientes/escritorio-java
mvn clean package
java -jar target/cliente-escritorio-java-1.0.0.jar http://192.168.1.50:8082/jakarta-rest-glassfish
```

Cliente web .NET:

```bash
cd clientes/web-dotnet
SERVER_REST_URL=http://192.168.1.50:5100 dotnet run
```

La web local del cliente .NET queda en:

```text
http://127.0.0.1:5200
```

Cliente web Java:

```bash
cd clientes/web-java
mvn clean package
```

Desplegar el WAR en Payara o GlassFish y configurar:

```bash
SERVER_REST_URL=http://192.168.1.50:8082/jakarta-rest-glassfish
```

## 5. Clientes moviles

Para emulador Android en la misma computadora que ejecuta la API, `10.0.2.2` apunta al host.

Para telefono fisico u otra computadora, usar la IP del servidor:

```text
http://192.168.1.50:5100/api/productos
```

En Android Java, cambiar la constante `server` en `clientes/movil-android-java/app/src/main/java/ec/edu/arquitectura/movil/MainActivity.java`.

En .NET MAUI, cambiar el `BaseAddress` en `clientes/movil-dotnet-maui/MauiProgram.cs`.

El telefono debe estar en la misma red Wi-Fi que el servidor y el firewall debe permitir el puerto `5100`.

## 6. Checklist rapido

- Servidor y cliente estan en la misma red.
- El servidor responde a `curl http://localhost:5100/api/productos`.
- Desde el cliente responde `curl http://IP_DEL_SERVIDOR:5100/api/productos`.
- Desde el cliente responde `curl http://IP_DEL_SERVIDOR:8082/jakarta-rest-glassfish/api/productos`.
- La web servidor abre en `http://IP_DEL_SERVIDOR:8081/jakarta-mvc-payara/login`.
- `SERVER_HOST` en `.env` contiene la IP del servidor.
- El firewall del servidor permite los puertos necesarios.
- Los clientes usan `http://IP_DEL_SERVIDOR:PUERTO`, no `localhost`.
