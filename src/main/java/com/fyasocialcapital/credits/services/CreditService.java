package com.fyasocialcapital.credits.service;

import com.fyasocialcapital.credits.model.Credit;
import com.fyasocialcapital.credits.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {
    
    private final CreditRepository creditRepository;
    private final EmailService emailService;
    
    @Transactional
    public Credit createCredit(Credit credit) {
        log.info("Registrando nuevo crédito para cliente: {}", credit.getNombreCliente());
        
        Credit savedCredit = creditRepository.save(credit);
        
        emailService.sendCreditRegistrationEmail(savedCredit);
        
        return savedCredit;
    }
    
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }
    
    public List<Credit> searchCredits(String nombreCliente, String cedula, String nombreComercial) {
        if (nombreCliente == null && cedula == null && nombreComercial == null) {
            return getAllCredits();
        }
        return creditRepository.searchCredits(nombreCliente, cedula, nombreComercial);
    }
    
    public List<Credit> getCreditsSortedByDate() {
        return creditRepository.findAllByOrderByFechaRegistroDesc();
    }
    
    public List<Credit> getCreditsSortedByAmount() {
        return creditRepository.findAllByOrderByValorCreditoDesc();
    }
    
    public Credit getCreditById(Long id) {
        return creditRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Crédito no encontrado con ID: " + id));
    }
}