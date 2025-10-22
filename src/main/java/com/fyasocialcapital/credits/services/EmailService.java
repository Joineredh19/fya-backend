package com.fyasocialcapital.credits.service;

import com.fyasocialcapital.credits.model.Credit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${app.email.to}")
    private String emailTo;
    
    @Value("${app.email.from}")
    private String emailFrom;
    
    @Async
    public void sendCreditRegistrationEmail(Credit credit) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(emailTo);
            message.setSubject("Nuevo Crédito Registrado - " + credit.getNombreCliente());
            message.setText(buildEmailContent(credit));
            
            mailSender.send(message);
            log.info("Email enviado exitosamente para el crédito ID: {}", credit.getId());
            
        } catch (Exception e) {
            log.error("Error al enviar email para el crédito ID: {}", credit.getId(), e);
            // No lanzamos excepción para no afectar el registro del crédito
        }
    }
    
    private String buildEmailContent(Credit credit) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        return String.format("""
            ====================================
            NUEVO CRÉDITO REGISTRADO
            ====================================
            
            Nombre del Cliente: %s
            Valor del Crédito: %s
            Nombre del Comercial: %s
            Fecha de Registro: %s
            
            ====================================
            Sistema de Gestión de Créditos
            Fya Social Capital SAS
            ====================================
            """,
            credit.getNombreCliente(),
            currencyFormat.format(credit.getValorCredito()),
            credit.getNombreComercial(),
            credit.getFechaRegistro().format(dateFormatter)
        );
    }
}