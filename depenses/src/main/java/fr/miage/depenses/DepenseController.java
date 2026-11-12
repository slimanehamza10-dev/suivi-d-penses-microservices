package fr.miage.depenses;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/depenses")
public class DepenseController {

    private final DepenseService service;

    public DepenseController(DepenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Depense> creer(
            @Valid @RequestBody CreationDepenseRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.creer(request));
    }

    @GetMapping
    public List<Depense> lister() {
        return service.lister();
    }
}