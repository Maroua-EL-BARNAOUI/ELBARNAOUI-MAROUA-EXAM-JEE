package org.sid.exam.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sid.exam.entities.StatutCredit;
import org.sid.exam.entities.TypeBien;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreditDTO {
    private Long id;
    private LocalDate dateDemande;
    private StatutCredit statut;
    private LocalDate dateAcceptation;
    private BigDecimal montant;
    private Integer dureeRemboursement;
    private BigDecimal tauxInteret;
    private Long clientId;
    private String clientNom;
    private String typeCredit;
    private String motif;
    private TypeBien typeBien;
    private String raisonSociale;
}
