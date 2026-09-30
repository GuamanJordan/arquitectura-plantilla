<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head><title>Login</title></head>
<body>
<h1>Arquitectura - Login</h1>
<% if (request.getAttribute("error") != null) { %>
<p><%= request.getAttribute("error") %></p>
<% } %>
<form method="post">
    <label>Usuario <input name="username" value="admin"></label>
    <label>Clave <input name="password" type="password" value="admin"></label>
    <button type="submit">Entrar</button>
</form>
</body>
</html>
