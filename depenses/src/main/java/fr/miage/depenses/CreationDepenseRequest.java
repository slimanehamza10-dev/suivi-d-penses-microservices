package fr.miage.depenses;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreationDepenseRequest(
        @NotNull
        @DecimalMin(value = "0", inclusive = false)
        BigDecimal montant,

        @NotNull
        @Positive
        Long categorieId,

        @NotNull
        LocalDate date
) {
}