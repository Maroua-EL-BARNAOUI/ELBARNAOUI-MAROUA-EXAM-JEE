package org.sid.exam.repositories;

import org.sid.exam.entities.Credit;
import org.sid.exam.entities.StatutCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByClientId(Long clientId);

    List<Credit> findByStatut(StatutCredit statut);
}
