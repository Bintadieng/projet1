package com.banque.service;

import com.banque.dto.*;
import com.banque.entity.Client;
import com.banque.entity.CompteBancaire;
import com.banque.entity.Transaction;
import com.banque.exception.InsufficientBalanceException;
import com.banque.exception.ResourceNotFoundException;
import com.banque.repository.ClientRepository;
import com.banque.repository.CompteBancaireRepository;
import com.banque.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BanqueService {

    private final ClientRepository clientRepository;
    private final CompteBancaireRepository compteRepository;
    private final TransactionRepository transactionRepository;

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
            .map(this::convertToClientDTO)
            .collect(Collectors.toList());
    }

    public SoldeDTO getSoldeByNumeroCompte(String numeroCompte) {
        CompteBancaire compte = compteRepository.findByNumeroCompte(numeroCompte)
            .orElseThrow(() -> new ResourceNotFoundException("Compte non trouvé: " + numeroCompte));

        return new SoldeDTO(
            compte.getId(),
            compte.getNumeroCompte(),
            compte.getSolde(),
            compte.getTypeCompte()
        );
    }

    @Transactional
    public CompteBancaire createCompte(CreateCompteRequest request) {
        Client client = clientRepository.findById(request.getClientId())
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé: " + request.getClientId()));

        CompteBancaire compte = new CompteBancaire();
        compte.setNumeroCompte(request.getNumeroCompte());
        compte.setClient(client);
        compte.setSolde(request.getSoldeInitial());
        compte.setTypeCompte(request.getTypeCompte());

        return compteRepository.save(compte);
    }

    @Transactional
    public Transaction effectuerTransfert(TransfertRequest request) {
        CompteBancaire compteSource = compteRepository.findByNumeroCompte(request.getNumeroCompteSource())
            .orElseThrow(() -> new ResourceNotFoundException("Compte source non trouvé: " + request.getNumeroCompteSource()));

        CompteBancaire compteDestination = compteRepository.findByNumeroCompte(request.getNumeroCompteDestination())
            .orElseThrow(() -> new ResourceNotFoundException("Compte destination non trouvé: " + request.getNumeroCompteDestination()));

        if (compteSource.getSolde().compareTo(request.getMontant()) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant pour effectuer le transfert");
        }

        compteSource.setSolde(compteSource.getSolde().subtract(request.getMontant()));
        compteDestination.setSolde(compteDestination.getSolde().add(request.getMontant()));

        compteRepository.save(compteSource);
        compteRepository.save(compteDestination);

        Transaction transaction = new Transaction();
        transaction.setCompteSource(compteSource);
        transaction.setCompteDestination(compteDestination);
        transaction.setMontant(request.getMontant());
        transaction.setTypeTransaction("transfert");
        transaction.setDescription(request.getDescription());

        return transactionRepository.save(transaction);
    }

    private ClientDTO convertToClientDTO(Client client) {
        return new ClientDTO(
            client.getId(),
            client.getNom(),
            client.getPrenom(),
            client.getEmail(),
            client.getTelephone()
        );
    }
}
