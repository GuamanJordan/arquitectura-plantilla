# REST, SOAP y gRPC

## REST

Contrato base:

```text
GET    /api/productos
GET    /api/productos/{id}
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}
```

## SOAP

Operaciones:

```text
listarProductos()
buscarProductoPorId(id)
crearProducto(producto)
```

## gRPC

Contrato en `servidores/dotnet-grpc-api/Protos/producto.proto`.
