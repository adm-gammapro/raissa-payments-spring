package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
