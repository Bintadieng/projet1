package com.banque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompteRequest {

    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String numeroCompte;

    @NotNull(message = "Le client ID est obligatoire")
    private UUID clientId;

    private BigDecimal soldeInitial = BigDecimal.ZERO;

    @NotBlank(message = "Le type de compte est obligatoire")
    private String typeCompte = "courant";
}
