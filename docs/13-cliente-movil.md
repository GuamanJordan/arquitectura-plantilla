# Guia para cliente movil

Esta guia explica como probar los clientes moviles contra una computadora servidor.

## Datos del servidor

Ejemplo usado en clase:

```text
IP servidor: 192.168.100.13
REST .NET: http://192.168.100.13:5100/api/productos
GlassFish REST: http://192.168.100.13:8082/jakarta-rest-glassfish/api/productos
Payara MVC: http://192.168.100.13:8081/jakarta-mvc-payara/login
```

El telefono debe estar conectado a la misma red Wi-Fi que el servidor.

## Android Java

Ruta:

```bash
clientes/movil-android-java
```

Archivo donde se configura el servidor:

```text
clientes/movil-android-java/app/src/main/java/ec/edu/arquitectura/movil/MainActivity.java
```

Para telefono fisico, usar la IP real del servidor:

```java
private final String server = "http://192.168.100.13:5100";
```

Para emulador Android cuando la API corre en la misma computadora:

```java
private final String server = "http://10.0.2.2:5100";
```

Compilar desde Android Studio o con Gradle:

```bash
cd clientes/movil-android-java
gradle assembleDebug
```

Si el proyecto tiene Gradle Wrapper:

```bash
./gradlew assembleDebug
```

APK generado normalmente:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## .NET MAUI Android

Ruta:

```bash
clientes/movil-dotnet-maui
```

Archivo donde se configura el servidor:

```text
clientes/movil-dotnet-maui/MauiProgram.cs
```

Para telefono fisico:

```csharp
builder.Services.AddSingleton(new HttpClient { BaseAddress = new Uri("http://192.168.100.13:5100") });
```

Para emulador Android con API local:

```csharp
builder.Services.AddSingleton(new HttpClient { BaseAddress = new Uri("http://10.0.2.2:5100") });
```

Instalar workloads:

```bash
dotnet workload install android maui-android
```

Compilar:

```bash
cd clientes/movil-dotnet-maui
dotnet build -f net10.0-android
```

APK generado normalmente:

```text
bin/Debug/net10.0-android/*.apk
```

## Permisos y red

Android Java ya incluye:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Como la plantilla usa HTTP para laboratorio, si una version de Android bloquea trafico no cifrado, habilitar cleartext en el cliente movil o usar HTTPS en el servidor.

## Checklist

- Telefono y servidor estan en la misma Wi-Fi.
- Desde otra computadora responde `curl http://192.168.100.13:5100/api/productos`.
- En el movil se usa `192.168.100.13`, no `localhost`.
- En emulador solo usar `10.0.2.2` si la API corre en la misma computadora.
- Firewall del servidor permite el puerto `5100`.
- La app muestra productos al presionar `Listar productos`.
