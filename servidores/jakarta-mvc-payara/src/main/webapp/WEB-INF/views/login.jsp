<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css">
</head>
<body>
<main class="login-shell">
    <section class="panel login-panel">
        <h1>Arquitectura</h1>
        <p>Aplicacion web MVC</p>
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post">
            <label>Usuario <input name="username" value="admin"></label>
            <label>Clave <input name="password" type="password" value="admin"></label>
            <button type="submit">Entrar</button>
        </form>
    </section>
</main>
</body>
</html>
