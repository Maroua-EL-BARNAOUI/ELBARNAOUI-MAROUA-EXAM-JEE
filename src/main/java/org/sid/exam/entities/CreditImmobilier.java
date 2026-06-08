package org.sid.exam.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("IMMOBILIER")
@Getter
@Setter
@NoArgsConstructor
public class CreditImmobilier extends Credit {

    @Enumerated(EnumType.STRING)
    private TypeBien typeBien;
}
