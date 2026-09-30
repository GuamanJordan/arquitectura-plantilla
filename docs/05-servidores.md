# Servidores

## GlassFish

Usado para Jakarta REST y SOAP.

```bash
cd servidores/jakarta-rest-glassfish
mvn clean package
```

## Payara

Usado para el flujo MVC.

```bash
cd servidores/jakarta-mvc-payara
mvn clean package
```

## .NET

```bash
cd servidores/dotnet-rest-api/src
dotnet run
```

```bash
cd servidores/dotnet-grpc-api
dotnet run
```

## IIS

Para Windows, publicar las apps .NET con:

```bash
dotnet publish -c Release
```

Luego configurar el sitio en IIS apuntando al directorio publicado.
