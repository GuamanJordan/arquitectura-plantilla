<%@ page import="ec.edu.arquitectura.mvc.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% Producto p = (Producto) request.getAttribute("producto"); %>
<!doctype html>
<html lang="es">
<head><title>Editar producto</title></head>
<body>
<h1>Editar producto</h1>
<form method="post" action="<%= request.getContextPath() %>/productos">
    <input type="hidden" name="id" value="<%= p.id %>">
    <input name="nombre" value="<%= p.nombre %>" required>
    <input name="descripcion" value="<%= p.descripcion %>">
    <input name="precio" value="<%= p.precio %>" required>
    <input name="stock" value="<%= p.stock %>" required>
    <button type="submit">Guardar</button>
</form>
</body>
</html>
