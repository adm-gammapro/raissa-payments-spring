package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsernameAndEstadoRegistro(String username, String estadoRegistro);

    List<UsuarioEntity> findByUsernameInAndEstadoRegistro(List<String> usernames, String estado);

    @Query("""
            SELECT DISTINCT u
            FROM UsuarioEntity u
            JOIN UsuarioClienteEntity uc ON uc.usuario.id = u.id
            JOIN UsuarioSistemaEntity us ON us.usuario.id = u.id AND us.sistema.id = '002'
            WHERE uc.estadoRegistro = 'S'
              AND uc.cliente.codigo = :idEmpresa
              AND (:estadoRegistro IS NULL OR u.estadoRegistro = :estadoRegistro)
              AND (:usernames IS NULL OR u.username NOT IN :usernames)
            """)
    List<UsuarioEntity> findByUsuariosDisponibles(List<String> usernames, Long idEmpresa, String estadoRegistro);
}
