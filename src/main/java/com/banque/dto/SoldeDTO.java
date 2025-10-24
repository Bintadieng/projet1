package com.banque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeDTO {
    private UUID compteId;
    private String numeroCompte;
    private BigDecimal solde;
    private String typeCompte;
}
