package org.sid.exam.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sid.exam.dto.ClientDTO;
import org.sid.exam.dto.CreditDTO;
import org.sid.exam.services.BankCreditService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Gestion des clients bancaires")
public class ClientRestController {

    private final BankCreditService bankCreditService;

    public ClientRestController(BankCreditService bankCreditService) {
        this.bankCreditService = bankCreditService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Lister tous les clients")
    public List<ClientDTO> listClients() {
        return bankCreditService.listClients();
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Consulter un client par son id")
    public ClientDTO getClient(@PathVariable Long clientId) {
        return bankCreditService.getClient(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creer un client")
    public ClientDTO saveClient(@RequestBody ClientDTO clientDTO) {
        return bankCreditService.saveClient(clientDTO);
    }

    @PutMapping("/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un client")
    public ClientDTO updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        clientDTO.setId(clientId);
        return bankCreditService.saveClient(clientDTO);
    }

    @GetMapping("/{clientId}/credits")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @Operation(summary = "Lister les credits d'un client")
    public List<CreditDTO> listCreditsByClient(@PathVariable Long clientId) {
        return bankCreditService.listCreditsByClient(clientId);
    }
}
