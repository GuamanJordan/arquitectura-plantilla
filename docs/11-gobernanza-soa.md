# Gobernanza SOA

Reglas propuestas:

- Versionar APIs con prefijo o encabezado: `v1`, `v2`.
- Documentar contratos REST, SOAP y gRPC antes de cambiar implementaciones.
- No exponer credenciales reales en el repositorio.
- Registrar errores con codigo, mensaje y correlacion.
- Mantener catalogo de servicios por responsable, puerto, contrato y version.
- Separar cambios incompatibles en ramas feature y documentarlos.

Catalogo inicial:

| Servicio | Contrato | Puerto |
| --- | --- | --- |
| Jakarta REST | `/api/productos` | 8082 |
| Jakarta SOAP | WSDL | 8082 |
| Jakarta MVC | Web MVC | 8081 |
| .NET REST | `/api/productos` | 5100 |
| .NET gRPC | `producto.proto` | 50051 |
