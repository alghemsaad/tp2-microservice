package ma.fpl.compteservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperationDto(

        @NotNull(message = "le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "le montant doit être strictement positif")
        BigDecimal montant
) {
}
