package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.ClienteDataSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDataSourceRepository extends JpaRepository<ClienteDataSourceEntity, Long> {
    ClienteDataSourceEntity findByClienteCodigoAndEstadoRegistro(Long codigoCliente, String estadoRegistro);
}
