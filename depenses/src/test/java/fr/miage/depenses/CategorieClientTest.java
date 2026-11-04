package fr.miage.depenses;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class CategorieClientTest {

    private HttpServer serveur;
    private ExecutorService executor;

    private CategorieClient demarrerServeur(int statut, boolean lent)
            throws Exception {

        serveur = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0), 0
        );

        executor = Executors.newCachedThreadPool();
        serveur.setExecutor(executor);

        serveur.createContext("/categories/1", exchange -> {
            try {
                if (lent) {
                    Thread.sleep(5000);
                }

                exchange.sendResponseHeaders(statut, -1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                exchange.close();
            }
        });

        serveur.start();

        int port = serveur.getAddress().getPort();

        return new CategorieClient("http://127.0.0.1:" + port);
    }

    @AfterEach
    void arreterServeur() {
        if (serveur != null) {
            serveur.stop(0);
        }

        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Test
    void accepteUneCategorieExistante() throws Exception {
        CategorieClient client = demarrerServeur(200, false);

        assertDoesNotThrow(() -> client.verifierExistence(1L));
    }

    @Test
    void transforme404En422() throws Exception {
        CategorieClient client = demarrerServeur(404, false);

        ResponseStatusException erreur = assertThrows(
                ResponseStatusException.class,
                () -> client.verifierExistence(1L)
        );

        assertEquals(422, erreur.getStatusCode().value());
    }

    @Test
    void transformeErreurServeurEn503() throws Exception {
        CategorieClient client = demarrerServeur(500, false);

        ResponseStatusException erreur = assertThrows(
                ResponseStatusException.class,
                () -> client.verifierExistence(1L)
        );

        assertEquals(503, erreur.getStatusCode().value());
    }

    @Test
    void transformeDelaiDepasseEn503() throws Exception {
        CategorieClient client = demarrerServeur(200, true);

        ResponseStatusException erreur = assertThrows(
                ResponseStatusException.class,
                () -> client.verifierExistence(1L)
        );

        assertEquals(503, erreur.getStatusCode().value());
        assertInstanceOf(HttpTimeoutException.class, erreur.getCause());
    }
}