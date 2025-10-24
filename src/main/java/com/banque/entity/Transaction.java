package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_source_id")
    private CompteBancaire compteSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_destination_id")
    private CompteBancaire compteDestination;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "type_transaction", nullable = false)
    private String typeTransaction;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
