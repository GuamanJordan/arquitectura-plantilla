<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head><title>Dashboard</title></head>
<body>
<h1>Dashboard</h1>
<p>Usuario: <%= session.getAttribute("usuario") %></p>
<nav>
    <a href="<%= request.getContextPath() %>/productos">Productos</a>
    <a href="<%= request.getContextPath() %>/logout">Salir</a>
</nav>
</body>
</html>
