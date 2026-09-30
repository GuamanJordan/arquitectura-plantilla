# Bases de datos

La entidad minima es `Producto`:

- `id`
- `nombre`
- `descripcion`
- `precio`
- `stock`

Para login/MVC se incluye `Usuario`:

- `id`
- `username`
- `password_hash`
- `rol`
- `activo`

Los scripts iniciales estan en:

- `docker/mariadb/init.sql`
- `docker/mysql/init.sql`
- `docker/sqlserver/init.sql`

MariaDB y MySQL ejecutan sus scripts al crear el volumen por primera vez. SQL Server requiere ejecutar:

```bash
./scripts/inicializar-sqlserver.sh
```

Las contrasenas del `.env.example` son academicas y deben cambiarse para cualquier entorno real.
