# Cliente movil Android Java

Proyecto Android Java minimo. En emulador Android, `10.0.2.2` apunta al `localhost` de la computadora anfitriona.

Para telefono fisico en la misma red que el servidor, cambiar la URL base en `MainActivity.java`:

```java
private final String server = "http://192.168.100.13:5100";
```

```bash
./gradlew assembleDebug
```

Si no existe Gradle Wrapper, abrir el proyecto en Android Studio o ejecutar con Gradle instalado.

Guia completa: `../../docs/13-cliente-movil.md`.
