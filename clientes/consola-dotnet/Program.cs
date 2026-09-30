using System.Net.Http.Json;
using Arquitectura.DotnetGrpcApi;
using Grpc.Net.Client;

var server = GetArg(args, "--server", "http://localhost:5100");
var grpcServer = GetArg(args, "--grpc", "http://localhost:50051");
var grpcOnly = args.Contains("--grpc-listar") && !args.Contains("--rest-listar") && !args.Contains("--crear");

if (!grpcOnly)
{
    using var http = new HttpClient { BaseAddress = new Uri(server) };
    var productos = await http.GetFromJsonAsync<List<Producto>>("/api/productos");

    Console.WriteLine("Productos por REST:");
    foreach (var producto in productos ?? [])
    {
        Console.WriteLine($"{producto.Id}: {producto.Nombre} - {producto.Precio:C}");
    }

    if (args.Contains("--crear"))
    {
        var response = await http.PostAsJsonAsync("/api/productos", new ProductoInput("Producto .NET", "Creado desde consola", 25.50m, 7));
        Console.WriteLine($"POST REST: {(int)response.StatusCode}");
        Console.WriteLine(await response.Content.ReadAsStringAsync());
    }
}

if (args.Contains("--grpc-listar"))
{
    using var channel = GrpcChannel.ForAddress(grpcServer);
    var client = new ProductoService.ProductoServiceClient(channel);
    var grpcProductos = await client.ListarProductosAsync(new ListarProductosRequest());
    Console.WriteLine("Productos por gRPC:");
    foreach (var producto in grpcProductos.Productos)
    {
        Console.WriteLine($"{producto.Id}: {producto.Nombre} - {producto.Precio}");
    }
}

static string GetArg(string[] args, string name, string fallback)
{
    for (var i = 0; i < args.Length - 1; i++)
    {
        if (args[i] == name)
        {
            return args[i + 1];
        }
    }
    return fallback;
}

record Producto(long Id, string Nombre, string? Descripcion, decimal Precio, int Stock);
record ProductoInput(string Nombre, string? Descripcion, decimal Precio, int Stock);
