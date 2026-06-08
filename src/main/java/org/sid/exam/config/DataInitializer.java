package org.sid.exam.config;

import org.sid.exam.entities.*;
import org.sid.exam.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(appUserRepository, passwordEncoder, "admin", "admin123", Set.of("ROLE_ADMIN"));
            createUserIfMissing(appUserRepository, passwordEncoder, "employe", "employe123", Set.of("ROLE_EMPLOYE"));
            createUserIfMissing(appUserRepository, passwordEncoder, "client", "client123", Set.of("ROLE_CLIEN"));
        };
    }

    @Bean
    CommandLineRunner initData(
            ClientRepository clientRepository,
            CreditRepository creditRepository,
            RemboursementRepository remboursementRepository
    ) {
        return args -> {
            if (clientRepository.count() > 0) return;

            Client c1 = clientRepository.save(new Client(null, "Ahmed Benali", "ahmed@gmail.com", null));
            Client c2 = clientRepository.save(new Client(null, "Sara Idrissi", "sara@gmail.com", null));
            Client c3 = clientRepository.save(new Client(null, "Youssef Alami", "youssef@gmail.com", null));

            CreditPersonnel cp1 = new CreditPersonnel();
            cp1.setClient(c1);
            cp1.setDateDemande(LocalDate.now());
            cp1.setStatut(StatutCredit.EN_COURS);
            cp1.setMontant(new BigDecimal("50000"));
            cp1.setDureeRemboursement(24);
            cp1.setTauxInteret(new BigDecimal("4.5"));
            cp1.setMotif("Achat voiture");
            creditRepository.save(cp1);

            CreditPersonnel cp2 = new CreditPersonnel();
            cp2.setClient(c2);
            cp2.setDateDemande(LocalDate.now());
            cp2.setStatut(StatutCredit.ACCEPTE);
            cp2.setDateAcceptation(LocalDate.now());
            cp2.setMontant(new BigDecimal("30000"));
            cp2.setDureeRemboursement(12);
            cp2.setTauxInteret(new BigDecimal("3.5"));
            cp2.setMotif("Études");
            creditRepository.save(cp2);

            CreditImmobilier ci1 = new CreditImmobilier();
            ci1.setClient(c1);
            ci1.setDateDemande(LocalDate.now());
            ci1.setStatut(StatutCredit.ACCEPTE);
            ci1.setDateAcceptation(LocalDate.now());
            ci1.setMontant(new BigDecimal("800000"));
            ci1.setDureeRemboursement(240);
            ci1.setTauxInteret(new BigDecimal("3.2"));
            ci1.setTypeBien(TypeBien.APPARTEMENT);
            creditRepository.save(ci1);

            CreditProfessionnel cpr1 = new CreditProfessionnel();
            cpr1.setClient(c3);
            cpr1.setDateDemande(LocalDate.now());
            cpr1.setStatut(StatutCredit.EN_COURS);
            cpr1.setMontant(new BigDecimal("200000"));
            cpr1.setDureeRemboursement(60);
            cpr1.setTauxInteret(new BigDecimal("5.0"));
            cpr1.setMotif("Achat matériel");
            cpr1.setRaisonSociale("Tech Solutions SARL");
            creditRepository.save(cpr1);

            Remboursement r1 = new Remboursement();
            r1.setCredit(cp2);
            r1.setDate(LocalDate.now());
            r1.setMontant(new BigDecimal("2600"));
            r1.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(r1);

            Remboursement r2 = new Remboursement();
            r2.setCredit(ci1);
            r2.setDate(LocalDate.now());
            r2.setMontant(new BigDecimal("3800"));
            r2.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(r2);

            Remboursement r3 = new Remboursement();
            r3.setCredit(cp1);
            r3.setDate(LocalDate.now());
            r3.setMontant(new BigDecimal("10000"));
            r3.setType(TypeRemboursement.REMBOURSEMENT_ANTICIPE);
            remboursementRepository.save(r3);

            System.out.println("✅ Données de test insérées !");
        };
    }

    private void createUserIfMissing(AppUserRepository appUserRepository,
                                     PasswordEncoder passwordEncoder,
                                     String username,
                                     String password,
                                     Set<String> roles) {
        if (!appUserRepository.existsByUsername(username)) {
            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPassword(passwordEncoder.encode(password));
            appUser.setRoles(roles);
            appUserRepository.save(appUser);
        }
    }
}