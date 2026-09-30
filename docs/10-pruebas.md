# Pruebas

## Infraestructura

```bash
cp .env.example .env
chmod +x scripts/*.sh
./scripts/verificar-entorno.sh
./scripts/levantar-infra.sh
./scripts/inicializar-sqlserver.sh
./scripts/probar-red.sh localhost
```

## REST

```bash
curl http://localhost:5100/api/productos
curl -X POST http://localhost:5100/api/productos \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Tablet","descripcion":"Prueba","precio":199.99,"stock":4}'
```

## Java

```bash
mvn clean package
```

## .NET

```bash
dotnet build
```
