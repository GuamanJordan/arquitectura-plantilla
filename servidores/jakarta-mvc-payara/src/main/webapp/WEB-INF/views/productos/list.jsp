<%@ page import="java.util.List" %>
<%@ page import="ec.edu.arquitectura.mvc.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head><title>Productos</title></head>
<body>
<h1>Productos</h1>
<form method="post">
    <input name="nombre" placeholder="Nombre" required>
    <input name="descripcion" placeholder="Descripcion">
    <input name="precio" placeholder="Precio" required>
    <input name="stock" placeholder="Stock" required>
    <button type="submit">Crear</button>
</form>
<table border="1">
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
<p><a href="<%= request.getContextPath() %>/dashboard">Dashboard</a></p>
</body>
</html>
