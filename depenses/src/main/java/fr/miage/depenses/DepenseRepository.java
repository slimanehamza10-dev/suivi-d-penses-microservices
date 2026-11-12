package fr.miage.depenses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepenseRepository
        extends JpaRepository<Depense, Long> {
}