#!/usr/bin/env bash

set -u

ACTION=${1:-start}
RUNTIME_DIR=".runtime"
PIDS_FILE="$RUNTIME_DIR/clientes.pids"

detener_clientes() {
  if [ ! -f "$PIDS_FILE" ]; then
    echo "No hay clientes registrados para detener."
    exit 0
  fi

  while read -r pid nombre; do
    if [ -n "${pid:-}" ] && kill -0 "$pid" >/dev/null 2>&1; then
      echo "Deteniendo $nombre (PID $pid)"
      kill "$pid" >/dev/null 2>&1 || true
    fi
  done < "$PIDS_FILE"

  rm -f "$PIDS_FILE"
  echo "Clientes detenidos."
}

if [ "$ACTION" = "stop" ] || [ "$ACTION" = "detener" ] || [ "$ACTION" = "apagar" ]; then
  detener_clientes
  exit 0
fi

if [ -f .env ]; then
  set -a
  . ./.env
  set +a
fi

mkdir -p "$RUNTIME_DIR"
: > "$PIDS_FILE"

if [ "$ACTION" = "start" ] || [ "$ACTION" = "levantar" ]; then
  SERVER_ARG=${2:-}
else
  SERVER_ARG=$ACTION
fi

SERVER_HOST=${SERVER_ARG:-${SERVER_HOST:-localhost}}
GLASSFISH_APP_PORT=${GLASSFISH_APP_PORT:-8082}
DOTNET_REST_PORT=${DOTNET_REST_PORT:-5100}
GRPC_PORT=${GRPC_PORT:-50051}
WEB_DOTNET_CLIENT_PORT=${WEB_DOTNET_CLIENT_PORT:-5200}

JAKARTA_REST_BASE="http://${SERVER_HOST}:${GLASSFISH_APP_PORT}/jakarta-rest-glassfish"
DOTNET_REST_BASE="http://${SERVER_HOST}:${DOTNET_REST_PORT}"
GRPC_BASE="http://${SERVER_HOST}:${GRPC_PORT}"

ok() {
  echo "OK: $1"
}

warn() {
  echo "AVISO: $1"
}

skip() {
  echo "OMITIDO: $1"
}

registrar_pid() {
  local pid=$1
  local nombre=$2
  echo "$pid $nombre" >> "$PIDS_FILE"
}

run_step() {
  local nombre=$1
  shift
  echo
  echo "===== $nombre ====="
  if "$@"; then
    ok "$nombre"
  else
    warn "$nombre fallo o no esta disponible. Revisa el mensaje anterior."
  fi
}

probar_red() {
  if [ -x scripts/probar-red.sh ]; then
    scripts/probar-red.sh "$SERVER_HOST"
  else
    curl -I "${DOTNET_REST_BASE}/api/productos"
  fi
}

cliente_consola_java() {
  command -v mvn >/dev/null 2>&1 || { skip "Maven no instalado"; return 1; }
  command -v java >/dev/null 2>&1 || { skip "Java no instalado"; return 1; }
  (
    cd clientes/consola-java &&
    mvn -q -DskipTests package &&
    java -jar target/cliente-consola-java-1.0.0.jar --server "$JAKARTA_REST_BASE"
  )
}

cliente_consola_dotnet_rest() {
  command -v dotnet >/dev/null 2>&1 || { skip ".NET no instalado"; return 1; }
  dotnet run --project clientes/consola-dotnet -- --server "$DOTNET_REST_BASE"
}

cliente_consola_dotnet_grpc() {
  command -v dotnet >/dev/null 2>&1 || { skip ".NET no instalado"; return 1; }
  dotnet run --project clientes/consola-dotnet -- --grpc "$GRPC_BASE" --grpc-listar
}

cliente_web_dotnet() {
  command -v dotnet >/dev/null 2>&1 || { skip ".NET no instalado"; return 1; }
  nohup env SERVER_REST_URL="$DOTNET_REST_BASE" dotnet run --project clientes/web-dotnet --urls "http://127.0.0.1:${WEB_DOTNET_CLIENT_PORT}" > "$RUNTIME_DIR/web-dotnet.log" 2>&1 &
  local pid=$!
  registrar_pid "$pid" "web-dotnet"
  echo "Cliente web .NET: http://127.0.0.1:${WEB_DOTNET_CLIENT_PORT}"
  echo "Log: $RUNTIME_DIR/web-dotnet.log"
}

cliente_escritorio_java() {
  command -v mvn >/dev/null 2>&1 || { skip "Maven no instalado"; return 1; }
  command -v java >/dev/null 2>&1 || { skip "Java no instalado"; return 1; }
  if [ -z "${DISPLAY:-}" ] && [ -z "${WAYLAND_DISPLAY:-}" ]; then
    skip "No hay entorno grafico para abrir Swing"
    return 1
  fi
  (
    cd clientes/escritorio-java &&
    mvn -q -DskipTests package
  )
  nohup java -jar clientes/escritorio-java/target/cliente-escritorio-java-1.0.0.jar "$JAKARTA_REST_BASE" > "$RUNTIME_DIR/escritorio-java.log" 2>&1 &
  local pid=$!
  registrar_pid "$pid" "escritorio-java"
  echo "Cliente escritorio Java iniciado. Log: $RUNTIME_DIR/escritorio-java.log"
}

cliente_web_java_build() {
  command -v mvn >/dev/null 2>&1 || { skip "Maven no instalado"; return 1; }
  (
    cd clientes/web-java &&
    mvn -q -DskipTests package
  )
  echo "WAR generado: clientes/web-java/target/cliente-web-java.war"
  echo "Desplegarlo en Payara o GlassFish con SERVER_REST_URL=$JAKARTA_REST_BASE"
}

cliente_android_java_build() {
  if [ -x clientes/movil-android-java/gradlew ]; then
    (cd clientes/movil-android-java && ./gradlew assembleDebug)
    return $?
  fi
  if command -v gradle >/dev/null 2>&1; then
    (cd clientes/movil-android-java && gradle assembleDebug)
    return $?
  fi
  skip "Gradle/Android Studio no disponible para compilar Android Java"
  return 1
}

cliente_maui_android_build() {
  command -v dotnet >/dev/null 2>&1 || { skip ".NET no instalado"; return 1; }
  (
    cd clientes/movil-dotnet-maui &&
    dotnet build -f net10.0-android
  )
}

echo "Servidor objetivo: $SERVER_HOST"
echo "Jakarta REST: $JAKARTA_REST_BASE"
echo ".NET REST:    $DOTNET_REST_BASE"
echo ".NET gRPC:    $GRPC_BASE"

run_step "Probar red contra el servidor" probar_red
run_step "Cliente consola Java REST" cliente_consola_java
run_step "Cliente consola .NET REST" cliente_consola_dotnet_rest
run_step "Cliente consola .NET gRPC" cliente_consola_dotnet_grpc
run_step "Cliente web .NET" cliente_web_dotnet
run_step "Cliente escritorio Java Swing" cliente_escritorio_java
run_step "Compilar cliente web Java" cliente_web_java_build
run_step "Compilar cliente movil Android Java" cliente_android_java_build
run_step "Compilar cliente movil .NET MAUI Android" cliente_maui_android_build

echo
echo "Clientes persistentes registrados en $PIDS_FILE"
echo "Para detener clientes en segundo plano:"
echo "  ./scripts/levantar-clientes.sh stop"
