package org.sid.exam.mappers;

import org.sid.exam.dto.RemboursementDTO;
import org.sid.exam.entities.Credit;
import org.sid.exam.entities.Remboursement;
import org.springframework.stereotype.Component;

@Component
public class RemboursementMapper {

    public RemboursementDTO toDTO(Remboursement remboursement) {
        if (remboursement == null) {
            return null;
        }
        Long creditId = remboursement.getCredit() != null ? remboursement.getCredit().getId() : null;
        return new RemboursementDTO(
                remboursement.getId(),
                remboursement.getDate(),
                remboursement.getMontant(),
                remboursement.getType(),
                creditId
        );
    }

    public Remboursement toEntity(RemboursementDTO remboursementDTO, Credit credit) {
        if (remboursementDTO == null) {
            return null;
        }
        Remboursement remboursement = new Remboursement();
        remboursement.setId(remboursementDTO.getId());
        remboursement.setDate(remboursementDTO.getDate());
        remboursement.setMontant(remboursementDTO.getMontant());
        remboursement.setType(remboursementDTO.getType());
        remboursement.setCredit(credit);
        return remboursement;
    }
}
