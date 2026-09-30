using System.Collections.Concurrent;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

var productos = new ConcurrentDictionary<long, Producto>();
var nextId = 1L;

Producto Seed(string nombre, string descripcion, decimal precio, int stock)
{
    var id = Interlocked.Increment(ref nextId) - 1;
    var producto = new Producto(id, nombre, descripcion, precio, stock);
    productos[id] = producto;
    return producto;
}

Seed("Laptop", "Equipo de prueba", 850.00m, 10);
Seed("Mouse", "Periferico de prueba", 15.50m, 50);
Seed("Teclado", "Periferico de prueba", 28.90m, 30);

app.MapGet("/", () => Results.Redirect("/api/productos"));

app.MapGet("/api/productos", () => productos.Values.OrderBy(p => p.Id));

app.MapGet("/api/productos/{id:long}", (long id) =>
    productos.TryGetValue(id, out var producto) ? Results.Ok(producto) : Results.NotFound());

app.MapPost("/api/productos", (ProductoInput input) =>
{
    var id = Interlocked.Increment(ref nextId) - 1;
    var producto = new Producto(id, input.Nombre, input.Descripcion, input.Precio, input.Stock);
    productos[id] = producto;
    return Results.Created($"/api/productos/{id}", producto);
});

app.MapPut("/api/productos/{id:long}", (long id, ProductoInput input) =>
{
    if (!productos.ContainsKey(id))
    {
        return Results.NotFound();
    }

    var producto = new Producto(id, input.Nombre, input.Descripcion, input.Precio, input.Stock);
    productos[id] = producto;
    return Results.Ok(producto);
});

app.MapDelete("/api/productos/{id:long}", (long id) =>
    productos.TryRemove(id, out _) ? Results.NoContent() : Results.NotFound());

app.Run();

record Producto(long Id, string Nombre, string? Descripcion, decimal Precio, int Stock);
record ProductoInput(string Nombre, string? Descripcion, decimal Precio, int Stock);
