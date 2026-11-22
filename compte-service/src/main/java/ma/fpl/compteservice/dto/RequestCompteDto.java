package ma.fpl.compteservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RequestCompteDto(

        @NotBlank(message = "le nom est obligatoire")
        String nom,

        @NotBlank(message = "le tel est obligatoire")
        String tel,

        @NotNull(message = "le solde est obligatoire")
        @PositiveOrZero(message = "le solde ne peut pas être négatif")
        BigDecimal solde
) {
}
