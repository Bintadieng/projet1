package com.banque.controller;

import com.banque.dto.*;
import com.banque.entity.CompteBancaire;
import com.banque.entity.Transaction;
import com.banque.service.BanqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BanqueController {

    private final BanqueService banqueService;

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clients = banqueService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/solde")
    public ResponseEntity<SoldeDTO> getSolde(@RequestParam String numeroCompte) {
        SoldeDTO solde = banqueService.getSoldeByNumeroCompte(numeroCompte);
        return ResponseEntity.ok(solde);
    }

    @PostMapping("/create")
    public ResponseEntity<CompteBancaire> createCompte(@Valid @RequestBody CreateCompteRequest request) {
        CompteBancaire compte = banqueService.createCompte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(compte);
    }

    @PostMapping("/transfere")
    public ResponseEntity<Transaction> effectuerTransfert(@Valid @RequestBody TransfertRequest request) {
        Transaction transaction = banqueService.effectuerTransfert(request);
        return ResponseEntity.ok(transaction);
    }
}
