import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MockCategories {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 18080), 0
        );

        server.createContext("/categories/", exchange -> {
            boolean existe = exchange.getRequestURI()
                    .getPath().equals("/categories/1");

            int statut = existe ? 200 : 404;

            String json = existe
                    ? "{\"id\":1,\"nom\":\"Alimentation\"}"
                    : "{\"message\":\"Categorie inexistante\"}";

            byte[] contenu = json.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type", "application/json; charset=utf-8"
            );

            exchange.sendResponseHeaders(statut, contenu.length);

            try (var sortie = exchange.getResponseBody()) {
                sortie.write(contenu);
            }
        });

        server.start();
        System.out.println("Categories simulees sur http://localhost:18080");
    }
}