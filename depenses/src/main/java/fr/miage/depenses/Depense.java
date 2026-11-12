package fr.miage.depenses;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "depenses")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private Long categorieId;

    @Column(nullable = false)
    private LocalDate date;

    protected Depense() {
        // Constructeur utilise par JPA.
    }

    public Depense(BigDecimal montant, Long categorieId, LocalDate date) {
        this.montant = montant;
        this.categorieId = categorieId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public LocalDate getDate() {
        return date;
    }
}