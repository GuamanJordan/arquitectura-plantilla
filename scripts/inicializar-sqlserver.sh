#!/usr/bin/env bash

set -e

if [ -f .env ]; then
  set -a
  . ./.env
  set +a
fi

SQLSERVER_SA_PASSWORD=${SQLSERVER_SA_PASSWORD:-Arquitectura2026!}
CONTAINER=${SQLSERVER_CONTAINER:-arquitectura-sqlserver}

echo "Esperando SQL Server en el contenedor $CONTAINER..."
for intento in $(seq 1 30); do
  if docker exec "$CONTAINER" /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$SQLSERVER_SA_PASSWORD" -C -Q "SELECT 1" >/dev/null 2>&1; then
    break
  fi
  if [ "$intento" -eq 30 ]; then
    echo "SQL Server no respondio a tiempo"
    exit 1
  fi
  sleep 2
done

docker exec "$CONTAINER" /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$SQLSERVER_SA_PASSWORD" -C -i /init.sql
echo "SQL Server inicializado"
