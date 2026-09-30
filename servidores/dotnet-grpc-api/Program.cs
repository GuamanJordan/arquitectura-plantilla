using System.Collections.Concurrent;
using Arquitectura.DotnetGrpcApi;
using Grpc.Core;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddGrpc();
builder.Services.AddSingleton<ProductoMemoryStore>();

var app = builder.Build();
app.MapGrpcService<ProductoGrpcService>();
app.MapGet("/", () => "Servicio gRPC de productos. Use un cliente gRPC para llamar ProductoService.");
app.Run();

public sealed class ProductoMemoryStore
{
    private readonly ConcurrentDictionary<long, ProductoReply> productos = new();
    private long nextId = 1;

    public ProductoMemoryStore()
    {
        Crear("Laptop", "Equipo de prueba", 850.00, 10);
        Crear("Mouse", "Periferico de prueba", 15.50, 50);
    }

    public IReadOnlyCollection<ProductoReply> Listar() => productos.Values.OrderBy(p => p.Id).ToList();

    public ProductoReply? Buscar(long id) => productos.TryGetValue(id, out var producto) ? producto : null;

    public ProductoReply Crear(string nombre, string descripcion, double precio, int stock)
    {
        var id = Interlocked.Increment(ref nextId) - 1;
        var producto = new ProductoReply
        {
            Id = id,
            Nombre = nombre,
            Descripcion = descripcion,
            Precio = precio,
            Stock = stock,
            Encontrado = true
        };
        productos[id] = producto;
        return producto;
    }
}

public sealed class ProductoGrpcService(ProductoMemoryStore store) : ProductoService.ProductoServiceBase
{
    public override Task<ListarProductosResponse> ListarProductos(ListarProductosRequest request, ServerCallContext context)
    {
        var response = new ListarProductosResponse();
        response.Productos.AddRange(store.Listar());
        return Task.FromResult(response);
    }

    public override Task<ProductoReply> BuscarProducto(BuscarProductoRequest request, ServerCallContext context)
    {
        return Task.FromResult(store.Buscar(request.Id) ?? new ProductoReply { Id = request.Id, Encontrado = false });
    }

    public override Task<ProductoReply> CrearProducto(CrearProductoRequest request, ServerCallContext context)
    {
        return Task.FromResult(store.Crear(request.Nombre, request.Descripcion, request.Precio, request.Stock));
    }
}
