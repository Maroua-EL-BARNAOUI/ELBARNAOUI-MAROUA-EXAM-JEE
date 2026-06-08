package org.sid.exam.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sid.exam.dto.RemboursementDTO;
import org.sid.exam.services.BankCreditService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Remboursements", description = "Gestion des remboursements des credits")
public class RemboursementRestController {

    private final BankCreditService bankCreditService;

    public RemboursementRestController(BankCreditService bankCreditService) {
        this.bankCreditService = bankCreditService;
    }

    @GetMapping("/credits/{creditId}/remboursements")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CLIENT')")
    @Operation(summary = "Lister les remboursements d'un credit")
    public List<RemboursementDTO> listRemboursementsByCredit(@PathVariable Long creditId) {
        return bankCreditService.listRemboursementsByCredit(creditId);
    }

    @PostMapping("/remboursements")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Ajouter un remboursement")
    public RemboursementDTO saveRemboursement(@RequestBody RemboursementDTO remboursementDTO) {
        return bankCreditService.saveRemboursement(remboursementDTO);
    }
}
