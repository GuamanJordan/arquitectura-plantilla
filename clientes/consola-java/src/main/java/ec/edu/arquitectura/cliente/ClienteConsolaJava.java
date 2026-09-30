package ec.edu.arquitectura.cliente;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteConsolaJava {
    public static void main(String[] args) throws Exception {
        String server = obtenerParametro(args, "--server", "http://localhost:8082/jakarta-rest-glassfish");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest listar = HttpRequest.newBuilder()
                .uri(URI.create(server + "/api/productos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(listar, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET /api/productos => " + response.statusCode());
        System.out.println(response.body());

        if (contiene(args, "--crear")) {
            String json = "{\"nombre\":\"Producto consola Java\",\"descripcion\":\"Creado desde cliente Java\",\"precio\":12.50,\"stock\":5}";
            HttpRequest crear = HttpRequest.newBuilder()
                    .uri(URI.create(server + "/api/productos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> creado = client.send(crear, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST /api/productos => " + creado.statusCode());
            System.out.println(creado.body());
        }
    }

    private static String obtenerParametro(String[] args, String nombre, String defecto) {
        for (int i = 0; i < args.length - 1; i++) {
            if (nombre.equals(args[i])) {
                return args[i + 1];
            }
        }
        return defecto;
    }

    private static boolean contiene(String[] args, String valor) {
        for (String arg : args) {
            if (valor.equals(arg)) {
                return true;
            }
        }
        return false;
    }
}
