package org.sid.exam.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("PROFESSIONNEL")
@Getter
@Setter
@NoArgsConstructor
public class CreditProfessionnel extends Credit {

    private String motif;
    private String raisonSociale;
}
