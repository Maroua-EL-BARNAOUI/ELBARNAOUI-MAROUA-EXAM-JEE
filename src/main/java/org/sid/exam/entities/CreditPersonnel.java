package org.sid.exam.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("PERSONNEL")
@Getter
@Setter
@NoArgsConstructor
public class CreditPersonnel extends Credit {

    private String motif;
}
