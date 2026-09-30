# Cliente movil .NET MAUI Android

Requisitos:

```bash
dotnet workload install android maui-android
```

Compilar:

```bash
dotnet build -f net10.0-android
```

En emulador Android, `10.0.2.2` apunta al `localhost` de la computadora anfitriona.

Para telefono fisico en la misma red que el servidor, cambiar `BaseAddress` en `MauiProgram.cs`:

```csharp
new Uri("http://192.168.100.13:5100")
```

Guia completa: `../../docs/13-cliente-movil.md`.
