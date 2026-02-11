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
              JOIN UsuarioClienteEntity usucli ON usucli.cliente.codigo = :idEmpresa
              JOIN UsuarioEntity usuario ON usuario = usucli.usuario
              JOIN PerfilSistemaEntity ps ON ps.perfil = opcper.perfil AND ps.sistema.id = :codigoSistema
              JOIN UsuarioPerfilEntity up ON up.usuario = usuario AND up.perfil = ps.perfil
            WHERE usuario.username = :usuario
              AND menu.estadoRegistro = 'S'
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
              JOIN UsuarioClienteEntity usucli ON usucli.cliente.codigo = :idEmpresa
              JOIN UsuarioEntity usuario ON usuario = usucli.usuario
              JOIN PerfilSistemaEntity ps ON ps.perfil = opcper.perfil AND ps.sistema.id = :codigoSistema
              JOIN UsuarioPerfilEntity up ON up.usuario = usuario AND up.perfil = ps.perfil
            WHERE usuario.username = :usuario
              AND menu.estadoRegistro = 'S'
              AND menu.opcionPadre IS NOT NULL
            ORDER BY menu.modulo.codigo, menu.opcionPadre, menu.numeroOrden
            """)
    List<OpcionEntity> listMenuBase(@Param("usuario") String usuario,
                                    @Param("idEmpresa") Long idEmpresa,
                                    @Param("codigoSistema") String codigoSistema);
}