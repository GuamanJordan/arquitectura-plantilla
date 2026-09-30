# Clientes

## Consola Java

```bash
cd clientes/consola-java
mvn clean package
java -jar target/cliente-consola-java-1.0.0.jar --server http://localhost:8082/jakarta-rest-glassfish
```

## Consola .NET

```bash
cd clientes/consola-dotnet
dotnet run -- --server http://localhost:5100
```

## Escritorio Java

```bash
cd clientes/escritorio-java
mvn clean package
java -jar target/cliente-escritorio-java-1.0.0.jar http://localhost:8082/jakarta-rest-glassfish
```

## Web y movil

Cada carpeta contiene su propio `README.md` con comandos de ejecucion.
