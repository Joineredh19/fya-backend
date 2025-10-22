package com.fyasocialcapital.credits.controller;

import com.fyasocialcapital.credits.model.Credit;
import com.fyasocialcapital.credits.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
public class CreditController {
    
    private final CreditService creditService;
    
    @PostMapping("/crear")
    public ResponseEntity<?> createCredit(@Valid @RequestBody Credit credit) {
        try {
            Credit savedCredit = creditService.createCredit(credit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crédito registrado exitosamente");
            response.put("data", savedCredit);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al registrar el crédito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/todos")
    public ResponseEntity<List<Credit>> getAllCredits(
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) String nombreComercial,
            @RequestParam(required = false) String sortBy) {
        
        List<Credit> credits;
        
        if (nombreCliente != null || cedula != null || nombreComercial != null) {
            credits = creditService.searchCredits(nombreCliente, cedula, nombreComercial);
        }
        else if ("fecha".equalsIgnoreCase(sortBy)) {
            credits = creditService.getCreditsSortedByDate();
        } else if ("valor".equalsIgnoreCase(sortBy)) {
            credits = creditService.getCreditsSortedByAmount();
        }
        else {
            credits = creditService.getAllCredits();
        }
        
        return ResponseEntity.ok(credits);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCreditById(@PathVariable Long id) {
        try {
            Credit credit = creditService.getCreditById(id);
            return ResponseEntity.ok(credit);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Error del servidor: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}