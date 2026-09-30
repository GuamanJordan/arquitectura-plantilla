<%@ page import="java.util.List" %>
<%@ page import="ec.edu.arquitectura.mvc.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head>
    <title>Productos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css">
</head>
<body>
<header class="topbar">
    <h1>Productos</h1>
    <p>Catalogo administrado desde Jakarta MVC</p>
</header>
<main class="page">
    <section class="panel">
        <h2>Crear producto</h2>
        <form method="post">
            <div class="form-grid">
                <label>Nombre <input name="nombre" placeholder="Laptop" required></label>
                <label>Descripcion <input name="descripcion" placeholder="Equipo de prueba"></label>
                <label>Precio <input name="precio" placeholder="850.00" required></label>
                <label>Stock <input name="stock" placeholder="10" required></label>
            </div>
            <button type="submit">Crear</button>
        </form>
    </section>
    <section>
        <h2>Listado</h2>
        <table>
            <tr><th>ID</th><th>Nombre</th><th>Precio</th><th>Stock</th><th>Acciones</th></tr>
            <% for (Producto p : (List<Producto>) request.getAttribute("productos")) { %>
            <tr>
                <td><%= p.id %></td>
                <td><%= p.nombre %></td>
                <td><%= p.precio %></td>
                <td><%= p.stock %></td>
                <td><a href="<%= request.getContextPath() %>/productos/editar/<%= p.id %>">Editar</a></td>
            </tr>
            <% } %>
        </table>
    </section>
    <p><a href="<%= request.getContextPath() %>/dashboard">Volver al dashboard</a></p>
</main>
</body>
</html>
