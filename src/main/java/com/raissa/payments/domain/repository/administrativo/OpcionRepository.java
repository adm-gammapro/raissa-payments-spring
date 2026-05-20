package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.OpcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepository extends JpaRepository<OpcionEntity, Long> {
    @Query("""
            SELECT DISTINCT menu
            FROM OpcionEntity menu
              JOIN OpcionPerfilEntity opcper ON opcper.opcion = menu
              AND opcper.estadoRegistro = 'S'
              JOIN ConfiguracionUsuarioEntity config ON config.cliente.codigo = :idEmpresa
              AND config.usuario.username = :usuario
              AND config.sistema.id = :codigoSistema
              AND config.perfil = opcper.perfil
              AND config.estadoRegistro = 'S'
            WHERE menu.estadoRegistro = 'S'
              AND menu.opcionPadre IS NULL
            ORDER BY menu.modulo.codigo, menu.numeroOrden
            """)
    List<OpcionEntity> listMenuPadres(@Param("usuario") String usuario,
                                      @Param("idEmpresa") Long idEmpresa,
                                      @Param("codigoSistema") String codigoSistema);

    @Query("""
            SELECT DISTINCT menu
            FROM OpcionEntity menu
              JOIN OpcionPerfilEntity opcper ON opcper.opcion = menu
              AND opcper.estadoRegistro = 'S'
              JOIN ConfiguracionUsuarioEntity config ON config.cliente.codigo = :idEmpresa
              AND config.usuario.username = :usuario
              AND config.sistema.id = :codigoSistema
              AND config.perfil = opcper.perfil
              AND config.estadoRegistro = 'S'
            WHERE menu.estadoRegistro = 'S'
              AND menu.opcionPadre IS NOT NULL
            ORDER BY menu.modulo.codigo, menu.opcionPadre, menu.numeroOrden
            """)
    List<OpcionEntity> listMenuBase(@Param("usuario") String usuario,
                                    @Param("idEmpresa") Long idEmpresa,
                                    @Param("codigoSistema") String codigoSistema);

    @Query("SELECT o FROM OpcionEntity o " +
            "WHERE o.seleccionable = 'S' " +
            "AND o.estadoRegistro = :estadoRegistro " +
            "AND EXISTS (SELECT os FROM OpcionSistemaEntity os " +
            "            WHERE os.opcionMenu = o.codigo " +
            "            AND os.sistema.id = :sistemaId " +
            "            AND os.estadoRegistro = :estadoRegistro) " +
            "ORDER BY o.numeroOrden ASC")
    List<OpcionEntity> findOpcionesBySistemaAndSeleccionable(@Param("sistemaId") String sistemaId,
                                                             @Param("estadoRegistro") String estadoRegistro);

    List<OpcionEntity> findByCodigoInAndEstadoRegistro(List<Long> ids, String estadoRegistro);
}