package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilEntity, Long> {
    @Query("""
            SELECT DISTINCT p
            FROM PerfilEntity p
            JOIN PerfilSistemaEntity ps ON ps.perfil.codigo = p.codigo
            JOIN PerfilClienteEntity pc ON pc.perfil.codigo = ps.perfil.codigo
            WHERE pc.estadoRegistro = 'S'
              AND pc.cliente.codigo = :idEmpresa
              AND ps.sistema.id = :codigoSistema
              AND (:estadoRegistro IS NULL OR p.estadoRegistro = :estadoRegistro)
              AND (:descripcion IS NULL OR CAST(:descripcion AS string) IS NULL
              OR UPPER(p.descripcion) LIKE UPPER(CONCAT('%', CAST(:descripcion AS string), '%')))
              AND (:abreviatura IS NULL OR CAST(:abreviatura AS string) IS NULL
              OR UPPER(p.abreviatura) LIKE UPPER(CONCAT('%', CAST(:abreviatura AS string), '%')))
            """)
    Page<PerfilEntity> searchByEmpresa(@Param("estadoRegistro") String estadoRegistro,
                                       @Param("descripcion") String descripcion,
                                       @Param("abreviatura") String abreviatura,
                                       @Param("idEmpresa") Long idEmpresa,
                                       @Param("codigoSistema") String codigoSistema,
                                       Pageable pageable);

    boolean existsByDescripcion(String descripcion);

    @Query("SELECT DISTINCT p FROM PerfilEntity p " +
            "JOIN PerfilClienteEntity pc ON pc.perfil = p " +
            "JOIN UsuarioClienteEntity uc ON uc.cliente = pc.cliente " +
            "JOIN UsuarioEntity u ON uc.usuario = u " +
            "WHERE p.estadoRegistro = 'S' " +
            "AND pc.estadoRegistro = 'S' " +
            "AND uc.estadoRegistro = 'S' " +
            "AND u.estadoRegistro = 'S' " +
            "AND u.username = :username " +
            "ORDER BY p.descripcion ASC")
    List<PerfilEntity> findPerfilesByUsernameOrderByDescripcionAsc(@Param("username") String username);

    List<PerfilEntity> findByCodigoInAndEstadoRegistro(List<Long> ids, String estadoRegistro);
}
