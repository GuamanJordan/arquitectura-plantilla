#!/usr/bin/env bash

set -e

echo "===== JAVA ====="
java --version 2>&1 | head -1 || echo "Java no instalado"

echo "===== MAVEN ====="
mvn --version 2>/dev/null | head -2 || echo "Maven no instalado"

echo "===== DOTNET ====="
dotnet --list-sdks 2>/dev/null || echo ".NET no instalado"

echo "===== DOTNET WORKLOADS ====="
dotnet workload list 2>/dev/null || echo "Workloads .NET no disponibles"

echo "===== ANDROID ====="
adb --version 2>/dev/null | head -3 || echo "ADB no instalado o no esta en PATH"

echo "===== DOCKER ====="
docker --version 2>/dev/null || echo "Docker no instalado"

echo "===== DOCKER COMPOSE ====="
docker compose version 2>/dev/null || echo "Docker Compose no instalado"

echo "===== FLATPAK APPS ====="
flatpak list 2>/dev/null | grep -Ei 'netbeans|dbeaver|android' || true

echo "===== DOCKER IMAGES ====="
docker images --format '{{.Repository}}:{{.Tag}}' 2>/dev/null | grep -E 'temurin|maven|payara|glassfish|dotnet|mysql|mariadb|mssql|camunda|zeebe' || true
