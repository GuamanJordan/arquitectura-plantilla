package ec.edu.arquitectura.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/")
public class ProductosServlet extends HttpServlet {
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String server = System.getenv().getOrDefault("SERVER_REST_URL", "http://localhost:8082/jakarta-rest-glassfish");
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(server + "/api/productos")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<h1>Cliente web Java</h1>");
            resp.getWriter().println("<pre>" + escape(response.body()) + "</pre>");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            resp.sendError(500, ex.getMessage());
        }
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
