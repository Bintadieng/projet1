package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comptes_bancaires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "numero_compte", unique = true, nullable = false)
    private String numeroCompte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    @Column(name = "type_compte", nullable = false)
    private String typeCompte = "courant";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
