using System.Net.Http.Json;

namespace Arquitectura.ClienteMovilMaui;

public class MainPage : ContentPage
{
    private readonly HttpClient http;
    private readonly Label output = new() { Text = "Cliente movil .NET MAUI" };

    public MainPage(HttpClient http)
    {
        this.http = http;
        Title = "Productos";
        var button = new Button { Text = "Listar productos" };
        button.Clicked += async (_, _) => await CargarProductos();
        Content = new VerticalStackLayout
        {
            Padding = 24,
            Children = { button, new ScrollView { Content = output } }
        };
    }

    private async Task CargarProductos()
    {
        try
        {
            var productos = await http.GetFromJsonAsync<List<Producto>>("/api/productos") ?? [];
            output.Text = string.Join(Environment.NewLine, productos.Select(p => $"{p.Id}: {p.Nombre} - {p.Precio:C}"));
        }
        catch (Exception ex)
        {
            output.Text = ex.Message;
        }
    }
}

public record Producto(long Id, string Nombre, string? Descripcion, decimal Precio, int Stock);
