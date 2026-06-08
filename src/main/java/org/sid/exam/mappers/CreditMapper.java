package org.sid.exam.mappers;

import org.sid.exam.dto.CreditDTO;
import org.sid.exam.entities.Client;
import org.sid.exam.entities.Credit;
import org.sid.exam.entities.CreditImmobilier;
import org.sid.exam.entities.CreditPersonnel;
import org.sid.exam.entities.CreditProfessionnel;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    public CreditDTO toDTO(Credit credit) {
        if (credit == null) {
            return null;
        }
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setId(credit.getId());
        creditDTO.setDateDemande(credit.getDateDemande());
        creditDTO.setStatut(credit.getStatut());
        creditDTO.setDateAcceptation(credit.getDateAcceptation());
        creditDTO.setMontant(credit.getMontant());
        creditDTO.setDureeRemboursement(credit.getDureeRemboursement());
        creditDTO.setTauxInteret(credit.getTauxInteret());

        if (credit.getClient() != null) {
            creditDTO.setClientId(credit.getClient().getId());
            creditDTO.setClientNom(credit.getClient().getNom());
        }

        if (credit instanceof CreditPersonnel creditPersonnel) {
            creditDTO.setTypeCredit("PERSONNEL");
            creditDTO.setMotif(creditPersonnel.getMotif());
        } else if (credit instanceof CreditImmobilier creditImmobilier) {
            creditDTO.setTypeCredit("IMMOBILIER");
            creditDTO.setTypeBien(creditImmobilier.getTypeBien());
        } else if (credit instanceof CreditProfessionnel creditProfessionnel) {
            creditDTO.setTypeCredit("PROFESSIONNEL");
            creditDTO.setMotif(creditProfessionnel.getMotif());
            creditDTO.setRaisonSociale(creditProfessionnel.getRaisonSociale());
        }

        return creditDTO;
    }

    public CreditPersonnel toCreditPersonnel(CreditDTO creditDTO, Client client) {
        CreditPersonnel credit = new CreditPersonnel();
        fillCommonFields(credit, creditDTO, client);
        credit.setMotif(creditDTO.getMotif());
        return credit;
    }

    public CreditImmobilier toCreditImmobilier(CreditDTO creditDTO, Client client) {
        CreditImmobilier credit = new CreditImmobilier();
        fillCommonFields(credit, creditDTO, client);
        credit.setTypeBien(creditDTO.getTypeBien());
        return credit;
    }

    public CreditProfessionnel toCreditProfessionnel(CreditDTO creditDTO, Client client) {
        CreditProfessionnel credit = new CreditProfessionnel();
        fillCommonFields(credit, creditDTO, client);
        credit.setMotif(creditDTO.getMotif());
        credit.setRaisonSociale(creditDTO.getRaisonSociale());
        return credit;
    }

    private void fillCommonFields(Credit credit, CreditDTO creditDTO, Client client) {
        credit.setId(creditDTO.getId());
        credit.setDateDemande(creditDTO.getDateDemande());
        credit.setStatut(creditDTO.getStatut());
        credit.setDateAcceptation(creditDTO.getDateAcceptation());
        credit.setMontant(creditDTO.getMontant());
        credit.setDureeRemboursement(creditDTO.getDureeRemboursement());
        credit.setTauxInteret(creditDTO.getTauxInteret());
        credit.setClient(client);
    }
}
