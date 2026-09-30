# Arquitectura general

La plantilla permite dos modos:

1. Una sola computadora con `localhost`.
2. Varias computadoras en red local, donde una actua como servidor y otras como clientes.

Flujo base:

```text
Cliente consola / escritorio / web / movil
  -> REST / SOAP / gRPC
  -> Servidor de aplicacion
  -> Servicios de negocio
  -> Base de datos
```

Para red local, cambiar `SERVER_HOST` en `.env` por la IP del servidor.
