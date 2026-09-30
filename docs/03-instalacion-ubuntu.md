# Instalacion directa en Ubuntu

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk maven docker.io docker-compose-plugin curl netcat-openbsd
```

Validar:

```bash
java --version
mvn --version
dotnet --list-sdks
docker compose version
```

Payara y GlassFish pueden ejecutarse instalados localmente o mediante Docker Compose.
