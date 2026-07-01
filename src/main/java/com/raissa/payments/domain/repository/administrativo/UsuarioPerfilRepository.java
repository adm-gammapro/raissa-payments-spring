package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioPerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfilEntity, Long> {
    List<UsuarioPerfilEntity> findByUsuarioIdAndEstadoRegistro(Long usuarioId, String estadoRegistro);

    Optional<UsuarioPerfilEntity> findByUsuarioIdAndPerfilCodigoAndEstadoRegistro(Long usuarioId, Long perfilId, String estadoRegistro);

    boolean existsByUsuarioIdAndPerfilCodigoAndEstadoRegistro(Long usuarioId, Long perfilId, String estadoRegistro);

    @Modifying
    @Query("UPDATE UsuarioPerfilEntity up SET up.estadoRegistro = 'N' " +
            "WHERE up.usuario.id = :usuarioId AND up.perfil.codigo IN :perfilesIds AND up.estadoRegistro = 'S'")
    int desasignarPerfiles(@Param("usuarioId") Long usuarioId,
                           @Param("perfilesIds") List<Long> perfilesIds);

    @Query("SELECT up.perfil FROM UsuarioPerfilEntity up " +
            "WHERE up.usuario.id = :usuarioId " +
            "AND up.estadoRegistro = :estadoRegistro " +
            "ORDER BY up.perfil.descripcion ASC")
    List<PerfilEntity> findPerfilesByUsuarioId(@Param("usuarioId") Long usuarioId,
                                               @Param("estadoRegistro") String estadoRegistro);
}