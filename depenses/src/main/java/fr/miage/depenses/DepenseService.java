package fr.miage.depenses;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DepenseService {

    private final CategorieClient categorieClient;
    private final DepenseRepository repository;

    public DepenseService(
            CategorieClient categorieClient,
            DepenseRepository repository) {

        this.categorieClient = categorieClient;
        this.repository = repository;
    }

    public Depense creer(CreationDepenseRequest request) {
        categorieClient.verifierExistence(request.categorieId());

        Depense depense = new Depense(
                request.montant(),
                request.categorieId(),
                request.date()
        );

        return repository.save(depense);
    }

    public List<Depense> lister() {
        return repository.findAll(Sort.by("id"));
    }
}