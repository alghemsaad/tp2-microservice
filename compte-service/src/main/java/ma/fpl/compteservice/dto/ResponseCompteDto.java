package ma.fpl.compteservice.dto;

import java.math.BigDecimal;

public record ResponseCompteDto(
        Long id,
        String nom,
        String tel,
        BigDecimal solde
) {
}
