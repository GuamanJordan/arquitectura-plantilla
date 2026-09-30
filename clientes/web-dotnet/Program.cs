using System.Net.Http.Json;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddHttpClient("productos", client =>
{
    var server = builder.Configuration["SERVER_REST_URL"] ?? "http://localhost:5100";
    client.BaseAddress = new Uri(server);
});

var app = builder.Build();

app.MapGet("/", async (IHttpClientFactory factory) =>
{
    var client = factory.CreateClient("productos");
    var productos = await client.GetFromJsonAsync<List<Producto>>("/api/productos") ?? [];
    var rows = string.Join("", productos.Select(p => $"<tr><td>{p.Id}</td><td>{p.Nombre}</td><td>{p.Precio}</td><td>{p.Stock}</td></tr>"));
    return Results.Content($"""
        <!doctype html>
        <html lang="es">
        <head><title>Cliente web .NET</title></head>
        <body>
        <h1>Cliente web .NET</h1>
        <table border="1">
        <tr><th>ID</th><th>Nombre</th><th>Precio</th><th>Stock</th></tr>
        {rows}
        </table>
        </body>
        </html>
        """, "text/html");
});

app.Run();

record Producto(long Id, string Nombre, string? Descripcion, decimal Precio, int Stock);
