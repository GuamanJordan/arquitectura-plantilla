# Instalacion directa en Debian

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk maven docker.io docker-compose-plugin curl netcat-openbsd
```

Instalar .NET desde los repositorios de Microsoft cuando el paquete no este disponible en Debian:

```bash
dotnet --list-sdks
```

Para Android Java se recomienda Android Studio. Para MAUI Android:

```bash
dotnet workload install android maui-android
```
