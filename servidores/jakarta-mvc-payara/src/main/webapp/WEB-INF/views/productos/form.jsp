<%@ page import="ec.edu.arquitectura.mvc.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% Producto p = (Producto) request.getAttribute("producto"); %>
<!doctype html>
<html lang="es">
<head>
    <title>Editar producto</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css">
</head>
<body>
<header class="topbar">
    <h1>Editar producto</h1>
    <p>ID <%= p.id %></p>
</header>
<main class="page">
    <section class="panel">
        <form method="post" action="<%= request.getContextPath() %>/productos">
            <input type="hidden" name="id" value="<%= p.id %>">
            <div class="form-grid">
                <label>Nombre <input name="nombre" value="<%= p.nombre %>" required></label>
                <label>Descripcion <input name="descripcion" value="<%= p.descripcion %>"></label>
                <label>Precio <input name="precio" value="<%= p.precio %>" required></label>
                <label>Stock <input name="stock" value="<%= p.stock %>" required></label>
            </div>
            <div class="actions">
                <button type="submit">Guardar</button>
                <a class="button secondary" href="<%= request.getContextPath() %>/productos">Cancelar</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
