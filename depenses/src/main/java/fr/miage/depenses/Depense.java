package fr.miage.depenses;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Depense(
        Long id,
        BigDecimal montant,
        Long categorieId,
        LocalDate date
) {
}