package org.sid.exam.services;

import org.sid.exam.dto.ClientDTO;
import org.sid.exam.dto.CreditDTO;
import org.sid.exam.dto.RemboursementDTO;
import org.sid.exam.entities.StatutCredit;

import java.math.BigDecimal;
import java.util.List;

public interface BankCreditService {
    ClientDTO saveClient(ClientDTO clientDTO);

    ClientDTO getClient(Long clientId);

    List<ClientDTO> listClients();

    CreditDTO saveCreditPersonnel(CreditDTO creditDTO);

    CreditDTO saveCreditImmobilier(CreditDTO creditDTO);

    CreditDTO saveCreditProfessionnel(CreditDTO creditDTO);

    CreditDTO getCredit(Long creditId);

    List<CreditDTO> listCredits();

    List<CreditDTO> listCreditsByClient(Long clientId);

    List<CreditDTO> listCreditsByStatut(StatutCredit statut);

    CreditDTO accepterCredit(Long creditId);

    CreditDTO rejeterCredit(Long creditId);

    RemboursementDTO saveRemboursement(RemboursementDTO remboursementDTO);

    List<RemboursementDTO> listRemboursementsByCredit(Long creditId);

    BigDecimal calculerMensualite(Long creditId);
}
