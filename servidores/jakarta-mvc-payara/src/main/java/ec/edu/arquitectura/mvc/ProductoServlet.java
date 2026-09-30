package ec.edu.arquitectura.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/productos/*")
public class ProductoServlet extends HttpServlet {
    private final ProductoStore store = ProductoStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!autenticado(req, resp)) {
            return;
        }
        String path = req.getPathInfo();
        if (path != null && path.startsWith("/editar/")) {
            long id = Long.parseLong(path.substring("/editar/".length()));
            req.setAttribute("producto", store.buscar(id).orElseThrow());
            req.getRequestDispatcher("/WEB-INF/views/productos/form.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("productos", store.listar());
        req.getRequestDispatcher("/WEB-INF/views/productos/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!autenticado(req, resp)) {
            return;
        }
        String id = req.getParameter("id");
        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        BigDecimal precio = new BigDecimal(req.getParameter("precio"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        if (id == null || id.isBlank()) {
            store.crear(nombre, descripcion, precio, stock);
        } else {
            store.actualizar(Long.parseLong(id), nombre, descripcion, precio, stock);
        }
        resp.sendRedirect(req.getContextPath() + "/productos");
    }

    private boolean autenticado(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession(false) == null || req.getSession(false).getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
