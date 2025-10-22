package com.fyasocialcapital.credits.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "creditos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Column(nullable = false)
    private String nombreCliente;
    
    @NotBlank(message = "La cédula o ID es obligatoria")
    @Column(nullable = false, unique = false)
    private String cedula;
    
    @NotNull(message = "El valor del crédito es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorCredito;
    
    @NotNull(message = "La tasa de interés es obligatoria")
    @DecimalMin(value = "0.0", message = "La tasa debe ser positiva")
    @DecimalMax(value = "100.0", message = "La tasa no puede exceder 100%")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteres;
    
    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo debe ser al menos 1 mes")
    @Column(nullable = false)
    private Integer plazoMeses;
    
    @NotBlank(message = "El nombre del comercial es obligatorio")
    @Column(nullable = false)
    private String nombreComercial;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}