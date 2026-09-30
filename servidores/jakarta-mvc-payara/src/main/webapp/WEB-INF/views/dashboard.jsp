<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="es">
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css">
</head>
<body>
<header class="topbar">
    <h1>Dashboard</h1>
    <p>Usuario: <%= session.getAttribute("usuario") %></p>
</header>
<main class="page">
    <section class="panel">
        <h2>Gestion de productos</h2>
        <p>Consulta y administra el catalogo usado por los servicios REST, SOAP y gRPC.</p>
        <nav class="actions">
            <a class="button" href="<%= request.getContextPath() %>/productos">Productos</a>
            <a class="button secondary" href="<%= request.getContextPath() %>/logout">Salir</a>
        </nav>
    </section>
</main>
</body>
</html>
