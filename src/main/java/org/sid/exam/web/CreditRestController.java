package org.sid.exam.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sid.exam.dto.CreditDTO;
import org.sid.exam.entities.StatutCredit;
import org.sid.exam.services.BankCreditService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@Tag(name = "Credits", description = "Gestion des demandes de credits")
public class CreditRestController {

    private final BankCreditService bankCreditService;

    public CreditRestController(BankCreditService bankCreditService) {
        this.bankCreditService = bankCreditService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Lister les credits", description = "Filtrer optionnellement par statut.")
    public List<CreditDTO> listCredits(@RequestParam(required = false) StatutCredit statut) {
        if (statut != null) {
            return bankCreditService.listCreditsByStatut(statut);
        }
        return bankCreditService.listCredits();
    }

    @GetMapping("/{creditId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Consulter un credit par son id")
    public CreditDTO getCredit(@PathVariable Long creditId) {
        return bankCreditService.getCredit(creditId);
    }

    @PostMapping("/personnels")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Creer un credit personnel")
    public CreditDTO saveCreditPersonnel(@RequestBody CreditDTO creditDTO) {
        return bankCreditService.saveCreditPersonnel(creditDTO);
    }

    @PostMapping("/immobiliers")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Creer un credit immobilier")
    public CreditDTO saveCreditImmobilier(@RequestBody CreditDTO creditDTO) {
        return bankCreditService.saveCreditImmobilier(creditDTO);
    }

    @PostMapping("/professionnels")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Creer un credit professionnel")
    public CreditDTO saveCreditProfessionnel(@RequestBody CreditDTO creditDTO) {
        return bankCreditService.saveCreditProfessionnel(creditDTO);
    }

    @PostMapping("/{creditId}/acceptation")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Accepter un credit")
    public CreditDTO accepterCredit(@PathVariable Long creditId) {
        return bankCreditService.accepterCredit(creditId);
    }

    @PostMapping("/{creditId}/rejet")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Rejeter un credit")
    public CreditDTO rejeterCredit(@PathVariable Long creditId) {
        return bankCreditService.rejeterCredit(creditId);
    }

    @GetMapping("/{creditId}/mensualite")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Calculer la mensualite theorique d'un credit")
    public BigDecimal calculerMensualite(@PathVariable Long creditId) {
        return bankCreditService.calculerMensualite(creditId);
    }
}
