using System.Net.Http.Json;
using System.Net;

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
    var totalStock = productos.Sum(p => p.Stock);
    var totalInventario = productos.Sum(p => p.Precio * p.Stock);
    var rows = string.Join("", productos.Select(p => $"""
        <tr>
          <td>{p.Id}</td>
          <td><strong>{Html(p.Nombre)}</strong><span>{Html(p.Descripcion ?? "Sin descripcion")}</span></td>
          <td>${p.Precio:N2}</td>
          <td>{p.Stock}</td>
        </tr>
        """));
    return Results.Content($$"""
        <!doctype html>
        <html lang="es">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <title>Cliente web .NET</title>
          <style>
            :root {
              color-scheme: light;
              --bg: #f5f7fb;
              --surface: #ffffff;
              --text: #172033;
              --muted: #667085;
              --border: #d9e2ef;
              --accent: #0f766e;
              --accent-weak: #e6f4f1;
            }
            * { box-sizing: border-box; }
            body {
              margin: 0;
              background: var(--bg);
              color: var(--text);
              font-family: Arial, Helvetica, sans-serif;
            }
            header {
              background: #172033;
              color: #fff;
              padding: 18px 28px;
            }
            header h1 {
              margin: 0;
              font-size: 24px;
            }
            header p {
              margin: 6px 0 0;
              color: #cbd5e1;
            }
            main {
              max-width: 1080px;
              margin: 24px auto;
              padding: 0 18px;
            }
            .metrics {
              display: grid;
              gap: 12px;
              grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
              margin-bottom: 18px;
            }
            .metric {
              background: var(--surface);
              border: 1px solid var(--border);
              border-radius: 8px;
              padding: 14px 16px;
            }
            .metric span {
              display: block;
              color: var(--muted);
              font-size: 13px;
            }
            .metric strong {
              display: block;
              margin-top: 6px;
              font-size: 24px;
            }
            .table-wrap {
              overflow-x: auto;
              background: var(--surface);
              border: 1px solid var(--border);
              border-radius: 8px;
            }
            table {
              width: 100%;
              border-collapse: collapse;
            }
            th, td {
              padding: 14px 16px;
              text-align: left;
              border-bottom: 1px solid var(--border);
              vertical-align: top;
            }
            th {
              color: var(--muted);
              font-size: 12px;
              text-transform: uppercase;
              background: #f8fafc;
            }
            tr:last-child td { border-bottom: 0; }
            td span {
              display: block;
              margin-top: 4px;
              color: var(--muted);
              font-size: 13px;
            }
            .status {
              display: inline-block;
              margin-top: 12px;
              padding: 6px 10px;
              border-radius: 999px;
              background: var(--accent-weak);
              color: var(--accent);
              font-size: 13px;
              font-weight: 700;
            }
          </style>
        </head>
        <body>
          <header>
            <h1>Cliente web .NET</h1>
            <p>Consumo REST de productos desde {{Html(client.BaseAddress?.ToString() ?? "servidor remoto")}}</p>
          </header>
          <main>
            <section class="metrics">
              <div class="metric"><span>Productos</span><strong>{{productos.Count}}</strong></div>
              <div class="metric"><span>Stock total</span><strong>{{totalStock}}</strong></div>
              <div class="metric"><span>Valor inventario</span><strong>${{totalInventario:N2}}</strong></div>
            </section>
            <section class="table-wrap">
              <table>
                <thead><tr><th>ID</th><th>Producto</th><th>Precio</th><th>Stock</th></tr></thead>
                <tbody>{{rows}}</tbody>
              </table>
            </section>
            <span class="status">Conectado</span>
          </main>
        </body>
        </html>
        """, "text/html");
});

app.Run();

static string Html(string value) => WebUtility.HtmlEncode(value);

record Producto(long Id, string Nombre, string? Descripcion, decimal Precio, int Stock);
