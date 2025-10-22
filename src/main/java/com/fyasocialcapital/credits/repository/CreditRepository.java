package com.fyasocialcapital.credits.repository;

import com.fyasocialcapital.credits.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    
    List<Credit> findByNombreClienteContainingIgnoreCase(String nombreCliente);
    
    List<Credit> findByCedulaContaining(String cedula);
    
    List<Credit> findByNombreComercialContainingIgnoreCase(String nombreComercial);
    
    @Query("SELECT c FROM Credit c WHERE " +
           "(:nombreCliente IS NULL OR LOWER(c.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) AND " +
           "(:cedula IS NULL OR c.cedula LIKE CONCAT('%', :cedula, '%')) AND " +
           "(:nombreComercial IS NULL OR LOWER(c.nombreComercial) LIKE LOWER(CONCAT('%', :nombreComercial, '%')))")
    List<Credit> searchCredits(@Param("nombreCliente") String nombreCliente,
                               @Param("cedula") String cedula,
                               @Param("nombreComercial") String nombreComercial);
    
    List<Credit> findAllByOrderByFechaRegistroDesc();
    
    List<Credit> findAllByOrderByValorCreditoDesc();
}