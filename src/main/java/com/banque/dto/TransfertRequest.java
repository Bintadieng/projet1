package com.banque.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransfertRequest {

    @NotBlank(message = "Le numéro de compte source est obligatoire")
    private String numeroCompteSource;

    @NotBlank(message = "Le numéro de compte destination est obligatoire")
    private String numeroCompteDestination;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    private String description;
}
