package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstitucionFinancieraRepository extends JpaRepository<InstitucionFinancieraEntity, String> {
    List<InstitucionFinancieraEntity> findByEstadoRegistro(String estadoRegistro);

    InstitucionFinancieraEntity findByCodigoAndEstadoRegistro(String codigo, String estadoRegistro);
}