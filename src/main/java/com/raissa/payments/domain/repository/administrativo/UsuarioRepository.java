package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT DISTINCT u
            FROM UsuarioEntity u
            JOIN UsuarioClienteEntity uc ON uc.usuario.id = u.id
            WHERE u.tipoUsuario = 'U'
              AND uc.estadoRegistro = 'S'
              AND uc.cliente.codigo IN (SELECT uccurrent.cliente.codigo FROM UsuarioClienteEntity uccurrent WHERE uccurrent.usuario.id = :userCurrent)
              AND (:estadoRegistro IS NULL OR u.estadoRegistro = :estadoRegistro)
              AND (:username IS NULL OR CAST(:username AS string) IS NULL OR UPPER(u.username) LIKE UPPER(CONCAT('%', CAST(:username AS string), '%')))
           """)
    Page<UsuarioEntity> searchByEmpresa(@Param("estadoRegistro") String estadoRegistro,
                                        @Param("username") String username,
                                        @Param("userCurrent") Long userCurrent,
                                        Pageable pageable);

    @Query("""
            SELECT u.username
            FROM UsuarioEntity u
            JOIN UsuarioClienteEntity uc ON uc.usuario = u
            JOIN UsuarioSistemaEntity us ON us.usuario = u AND us.sistema.id = '002'
            WHERE uc.estadoRegistro = 'S'
              AND us.estadoRegistro = 'S'
              AND u.estadoRegistro = 'S'
              AND uc.cliente.codigo = :idEmpresa
              AND (:searchTerm IS NULL OR :searchTerm = '' OR
                  LOWER(u.nombreCompletoBusqueda) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
            """)
    List<String> findUsersForNames(String searchTerm, Long idEmpresa);
}
