package fr.miage.depenses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DepenseService {

    private final CategorieClient categorieClient;
    private final List<Depense> depenses = new ArrayList<>();
    private long prochainId = 1;

    public DepenseService(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public Depense creer(CreationDepenseRequest request) {
        categorieClient.verifierExistence(request.categorieId());
        return enregistrer(request);
    }

    private synchronized Depense enregistrer(
            CreationDepenseRequest request) {

        Depense depense = new Depense(
                prochainId++,
                request.montant(),
                request.categorieId(),
                request.date()
        );

        depenses.add(depense);
        return depense;
    }

    public synchronized List<Depense> lister() {
        return List.copyOf(depenses);
    }
}