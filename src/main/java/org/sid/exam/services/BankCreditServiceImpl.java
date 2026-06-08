package org.sid.exam.services;

import org.sid.exam.dto.ClientDTO;
import org.sid.exam.dto.CreditDTO;
import org.sid.exam.dto.RemboursementDTO;
import org.sid.exam.entities.Client;
import org.sid.exam.entities.Credit;
import org.sid.exam.entities.Remboursement;
import org.sid.exam.entities.StatutCredit;
import org.sid.exam.exceptions.ResourceNotFoundException;
import org.sid.exam.mappers.ClientMapper;
import org.sid.exam.mappers.CreditMapper;
import org.sid.exam.mappers.RemboursementMapper;
import org.sid.exam.repositories.ClientRepository;
import org.sid.exam.repositories.CreditRepository;
import org.sid.exam.repositories.RemboursementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BankCreditServiceImpl implements BankCreditService {

    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final RemboursementRepository remboursementRepository;
    private final ClientMapper clientMapper;
    private final CreditMapper creditMapper;
    private final RemboursementMapper remboursementMapper;

    public BankCreditServiceImpl(ClientRepository clientRepository,
                                 CreditRepository creditRepository,
                                 RemboursementRepository remboursementRepository,
                                 ClientMapper clientMapper,
                                 CreditMapper creditMapper,
                                 RemboursementMapper remboursementMapper) {
        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
        this.remboursementRepository = remboursementRepository;
        this.clientMapper = clientMapper;
        this.creditMapper = creditMapper;
        this.remboursementMapper = remboursementMapper;
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        return clientMapper.toDTO(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClient(Long clientId) {
        return clientMapper.toDTO(findClientById(clientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> listClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    @Override
    public CreditDTO saveCreditPersonnel(CreditDTO creditDTO) {
        Client client = findClientById(creditDTO.getClientId());
        Credit credit = creditMapper.toCreditPersonnel(creditDTO, client);
        prepareNewCredit(credit);
        return creditMapper.toDTO(creditRepository.save(credit));
    }

    @Override
    public CreditDTO saveCreditImmobilier(CreditDTO creditDTO) {
        Client client = findClientById(creditDTO.getClientId());
        Credit credit = creditMapper.toCreditImmobilier(creditDTO, client);
        prepareNewCredit(credit);
        return creditMapper.toDTO(creditRepository.save(credit));
    }

    @Override
    public CreditDTO saveCreditProfessionnel(CreditDTO creditDTO) {
        Client client = findClientById(creditDTO.getClientId());
        Credit credit = creditMapper.toCreditProfessionnel(creditDTO, client);
        prepareNewCredit(credit);
        return creditMapper.toDTO(creditRepository.save(credit));
    }

    @Override
    @Transactional(readOnly = true)
    public CreditDTO getCredit(Long creditId) {
        return creditMapper.toDTO(findCreditById(creditId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditDTO> listCredits() {
        return creditRepository.findAll()
                .stream()
                .map(creditMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditDTO> listCreditsByClient(Long clientId) {
        return creditRepository.findByClientId(clientId)
                .stream()
                .map(creditMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditDTO> listCreditsByStatut(StatutCredit statut) {
        return creditRepository.findByStatut(statut)
                .stream()
                .map(creditMapper::toDTO)
                .toList();
    }

    @Override
    public CreditDTO accepterCredit(Long creditId) {
        Credit credit = findCreditById(creditId);
        credit.setStatut(StatutCredit.ACCEPTE);
        credit.setDateAcceptation(LocalDate.now());
        return creditMapper.toDTO(creditRepository.save(credit));
    }

    @Override
    public CreditDTO rejeterCredit(Long creditId) {
        Credit credit = findCreditById(creditId);
        credit.setStatut(StatutCredit.REJETE);
        credit.setDateAcceptation(null);
        return creditMapper.toDTO(creditRepository.save(credit));
    }

    @Override
    public RemboursementDTO saveRemboursement(RemboursementDTO remboursementDTO) {
        Credit credit = findCreditById(remboursementDTO.getCreditId());
        Remboursement remboursement = remboursementMapper.toEntity(remboursementDTO, credit);
        return remboursementMapper.toDTO(remboursementRepository.save(remboursement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RemboursementDTO> listRemboursementsByCredit(Long creditId) {
        return remboursementRepository.findByCreditId(creditId)
                .stream()
                .map(remboursementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculerMensualite(Long creditId) {
        Credit credit = findCreditById(creditId);
        BigDecimal montant = credit.getMontant();
        BigDecimal tauxMensuel = credit.getTauxInteret()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        int duree = credit.getDureeRemboursement();

        if (tauxMensuel.compareTo(BigDecimal.ZERO) == 0) {
            return montant.divide(BigDecimal.valueOf(duree), 2, RoundingMode.HALF_UP);
        }

        double taux = tauxMensuel.doubleValue();
        double mensualite = montant.doubleValue() * taux / (1 - Math.pow(1 + taux, -duree));
        return BigDecimal.valueOf(mensualite).setScale(2, RoundingMode.HALF_UP);
    }

    private void prepareNewCredit(Credit credit) {
        if (credit.getDateDemande() == null) {
            credit.setDateDemande(LocalDate.now());
        }
        if (credit.getStatut() == null) {
            credit.setStatut(StatutCredit.EN_COURS);
        }
    }

    private Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id " + clientId));
    }

    private Credit findCreditById(Long creditId) {
        return creditRepository.findById(creditId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit introuvable avec l'id " + creditId));
    }
}
