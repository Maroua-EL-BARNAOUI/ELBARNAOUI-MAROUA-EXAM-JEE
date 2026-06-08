package org.sid.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sid.exam.entities.TypeRemboursement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemboursementDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal montant;
    private TypeRemboursement type;
    private Long creditId;
}
