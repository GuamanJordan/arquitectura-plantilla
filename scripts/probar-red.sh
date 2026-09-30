#!/usr/bin/env bash

set -e

HOST=${1:-localhost}

echo "Probando servidor: $HOST"

nc -vz "$HOST" 8082 || true
nc -vz "$HOST" 8081 || true
nc -vz "$HOST" 3307 || true
nc -vz "$HOST" 3308 || true
nc -vz "$HOST" 1433 || true
nc -vz "$HOST" 5100 || true
nc -vz "$HOST" 50051 || true

curl -I "http://$HOST:8082" || true
curl -I "http://$HOST:8081" || true
curl -I "http://$HOST:5100/api/productos" || true
