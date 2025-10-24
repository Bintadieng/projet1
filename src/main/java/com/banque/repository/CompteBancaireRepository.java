package com.banque.repository;

import com.banque.entity.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, UUID> {
    Optional<CompteBancaire> findByNumeroCompte(String numeroCompte);
}
