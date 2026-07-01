package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistemaEntity, Long> {
    List<UsuarioSistemaEntity> findByUsuarioUsernameAndEstadoRegistro(String username, String estadoRegistro);

    List<UsuarioSistemaEntity> findByUsuarioIdAndEstadoRegistro(Long usuarioId, String estadoRegistro);

    Optional<UsuarioSistemaEntity> findByUsuarioIdAndSistemaIdAndEstadoRegistro(Long usuarioId, String sistemaId, String estadoRegistro);

    boolean existsByUsuarioIdAndSistemaIdAndEstadoRegistro(Long usuarioId, String sistemaId, String estadoRegistro);

    @Modifying
    @Query("UPDATE UsuarioSistemaEntity us SET us.estadoRegistro = 'N' " +
            "WHERE us.usuario.id = :usuarioId AND us.sistema.id IN :sistemasIds AND us.estadoRegistro = 'S'")
    int desasignarSistemas(@Param("usuarioId") Long usuarioId,
                           @Param("sistemasIds") List<String> sistemasIds);

    @Query("SELECT us.sistema FROM UsuarioSistemaEntity us " +
            "WHERE us.usuario.id = :usuarioId " +
            "AND us.estadoRegistro = :estadoRegistro " +
            "ORDER BY us.sistema.nombre ASC")
    List<SistemaEntity> findSistemasByUsuarioId(@Param("usuarioId") Long usuarioId,
                                                @Param("estadoRegistro") String estadoRegistro);
}
