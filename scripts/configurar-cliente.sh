#!/usr/bin/env bash

set -e

if [ $# -lt 1 ]; then
  echo "Uso: ./scripts/configurar-cliente.sh IP_DEL_SERVIDOR"
  echo "Ejemplo: ./scripts/configurar-cliente.sh 192.168.1.50"
  exit 1
fi

SERVER_HOST="$1"

if [ ! -f .env ]; then
  cp .env.example .env
fi

if grep -q '^SERVER_HOST=' .env; then
  sed -i "s/^SERVER_HOST=.*/SERVER_HOST=${SERVER_HOST}/" .env
else
  printf '\nSERVER_HOST=%s\n' "$SERVER_HOST" >> .env
fi

echo "Cliente configurado para consumir el servidor: $SERVER_HOST"
echo
echo "Endpoints principales:"
echo "  Jakarta REST: http://$SERVER_HOST:8082/jakarta-rest-glassfish/api/productos"
echo "  .NET REST:    http://$SERVER_HOST:5100/api/productos"
echo "  .NET gRPC:    http://$SERVER_HOST:50051"
echo
echo "Probar red:"
echo "  ./scripts/probar-red.sh $SERVER_HOST"
