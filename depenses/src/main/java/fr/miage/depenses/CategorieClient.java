package fr.miage.depenses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CategorieClient {

    private final String baseUrl;
    private final HttpClient client;

    public CategorieClient(
            @Value("${categories.base-url}") String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void verifierExistence(Long categorieId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/categories/" + categorieId))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        try {
            HttpResponse<Void> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.discarding()
            );

            if (response.statusCode() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.valueOf(422),
                        "La categorie demandee n'existe pas"
                );
            }

            if (response.statusCode() != 200) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Le service categories a retourne une reponse inattendue"
                );
            }

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Le service categories est indisponible ou trop lent",
                    e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "L'appel au service categories a ete interrompu",
                    e
            );
        }
    }
}