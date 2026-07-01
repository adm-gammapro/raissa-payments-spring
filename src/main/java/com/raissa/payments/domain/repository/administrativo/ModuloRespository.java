package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuloRespository extends JpaRepository<ModuloEntity, Long> {

    @Query("""
            SELECT DISTINCT m
            FROM ModuloEntity m
            WHERE EXISTS (
                SELECT 1
                FROM OpcionEntity o
                    JOIN o.modulo mod2
                    JOIN OpcionPerfilEntity op ON op.opcion = o
                    AND o.estadoRegistro = 'S'
                    JOIN ConfiguracionUsuarioEntity config ON config.cliente.codigo = :idEmpresa
                    AND config.usuario.username = :usuario
                    AND config.sistema.id = :codigoSistema
                    AND config.perfil = op.perfil
                    AND config.estadoRegistro = 'S'
                WHERE o.estadoRegistro = 'S'
                  AND o.opcionPadre IS NOT NULL
                  AND mod2 = m
            )
            """)
    List<ModuloEntity> listModulos(@Param("usuario") String usuario,
                                   @Param("idEmpresa") Long idEmpresa,
                                   @Param("codigoSistema") String codigoSistema);
}
