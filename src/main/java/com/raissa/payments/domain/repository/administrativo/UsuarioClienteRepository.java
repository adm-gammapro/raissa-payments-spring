package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioClienteRepository extends JpaRepository<UsuarioClienteEntity, Long> {
    List<UsuarioClienteEntity> findByUsuarioUsernameAndEstadoRegistro(String username, String estadoRegistro);

    List<UsuarioClienteEntity> findByUsuarioIdAndEstadoRegistro(Long usuarioId, String estadoRegistro);

    Optional<UsuarioClienteEntity> findByUsuarioIdAndClienteCodigoAndEstadoRegistro(Long usuarioId, Long clienteId, String estadoRegistro);

    boolean existsByUsuarioIdAndClienteCodigoAndEstadoRegistro(Long usuarioId, Long clienteId, String estadoRegistro);

    @Modifying
    @Query("UPDATE UsuarioClienteEntity uc SET uc.estadoRegistro = 'N' " +
            "WHERE uc.usuario.id = :usuarioId AND uc.cliente.codigo IN :clientesIds AND uc.estadoRegistro = 'S'")
    int desasignarClientes(@Param("usuarioId") Long usuarioId,
                           @Param("clientesIds") List<Long> clientesIds);

    // Método para obtener los clientes activos de un usuario
    @Query("SELECT uc.cliente FROM UsuarioClienteEntity uc " +
            "WHERE uc.usuario.id = :usuarioId " +
            "AND uc.estadoRegistro = :estadoRegistro " +
            "AND uc.cliente.estadoRegistro = :estadoRegistro " +
            "ORDER BY uc.cliente.razonSocial ASC")
    List<ClienteEntity> findClientesByUsuarioIdAndEstadoRegistro(@Param("usuarioId") Long usuarioId,
                                                                 @Param("estadoRegistro") String estadoRegistro);
}