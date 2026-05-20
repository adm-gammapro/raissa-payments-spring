package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.PerfilClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilClienteRepository extends JpaRepository<PerfilClienteEntity, Long> {
    List<PerfilClienteEntity> findByPerfilCodigoAndEstadoRegistro(Long perfilId, String estadoRegistro);

    Optional<PerfilClienteEntity> findByPerfilCodigoAndClienteCodigoAndEstadoRegistro(Long perfilId, Long clienteId, String estadoRegistro);

    boolean existsByPerfilCodigoAndClienteCodigoAndEstadoRegistro(Long perfilId, Long clienteId, String estadoRegistro);

    @Modifying
    @Query("UPDATE PerfilClienteEntity pc SET pc.estadoRegistro = 'N' " +
            "WHERE pc.perfil.codigo = :perfilId AND pc.cliente.codigo IN :clientesIds AND pc.estadoRegistro = 'S'")
    int desasignarClientes(@Param("perfilId") Long perfilId,
                           @Param("clientesIds") List<Long> clientesIds);
}