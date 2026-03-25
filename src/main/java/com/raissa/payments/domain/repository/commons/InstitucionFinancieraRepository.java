package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitucionFinancieraRepository extends JpaRepository<InstitucionFinancieraEntity, String> {
}