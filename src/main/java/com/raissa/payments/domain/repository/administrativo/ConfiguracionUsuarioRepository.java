package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ConfiguracionUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConfiguracionUsuarioRepository extends JpaRepository<ConfiguracionUsuarioEntity, Long> {
    List<ConfiguracionUsuarioEntity> findByUsuarioIdAndEstadoRegistroOrderByIdConfiguracionDesc(
            Long usuarioId, String estadoRegistro);

    Optional<ConfiguracionUsuarioEntity> findByIdConfiguracionAndEstadoRegistro(Long id, String estadoRegistro);

    @Query("SELECT c FROM ConfiguracionUsuarioEntity c " +
            "JOIN FETCH c.usuario u " +
            "JOIN FETCH c.cliente cl " +
            "JOIN FETCH c.perfil p " +
            "JOIN FETCH c.sistema s " +
            "WHERE c.usuario.id = :usuarioId " +
            "AND c.estadoRegistro = :estadoRegistro " +
            "ORDER BY c.idConfiguracion DESC")
    List<ConfiguracionUsuarioEntity> findConfiguracionesByUsuarioIdWithDetails(
            @Param("usuarioId") Long usuarioId,
            @Param("estadoRegistro") String estadoRegistro);

    // Verificar si ya existe una configuración para el mismo cliente
    @Query("SELECT COUNT(c) > 0 FROM ConfiguracionUsuarioEntity c " +
            "WHERE c.usuario.id = :usuarioId " +
            "AND c.cliente.codigo = :clienteId " +
            "AND c.estadoRegistro = :estadoRegistro " +
            "AND (:idConfiguracion IS NULL OR c.idConfiguracion != :idConfiguracion)")
    boolean existsByUsuarioIdAndClienteIdExcludingId(
            @Param("usuarioId") Long usuarioId,
            @Param("clienteId") Long clienteId,
            @Param("estadoRegistro") String estadoRegistro,
            @Param("idConfiguracion") Long idConfiguracion);

    // Verificar si ya existe la misma configuración exacta
    @Query("SELECT COUNT(c) > 0 FROM ConfiguracionUsuarioEntity c " +
            "WHERE c.usuario.id = :usuarioId " +
            "AND c.cliente.codigo = :clienteId " +
            "AND c.sistema.id = :sistemaId " +
            "AND c.perfil.codigo = :perfilId " +
            "AND c.estadoRegistro = :estadoRegistro " +
            "AND (:idConfiguracion IS NULL OR c.idConfiguracion != :idConfiguracion)")
    boolean existsDuplicateConfiguracion(
            @Param("usuarioId") Long usuarioId,
            @Param("clienteId") Long clienteId,
            @Param("sistemaId") String sistemaId,
            @Param("perfilId") Long perfilId,
            @Param("estadoRegistro") String estadoRegistro,
            @Param("idConfiguracion") Long idConfiguracion);
}