package org.sid.exam.repositories;

import org.sid.exam.entities.Remboursement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemboursementRepository extends JpaRepository<Remboursement, Long> {
    List<Remboursement> findByCreditId(Long creditId);
}
