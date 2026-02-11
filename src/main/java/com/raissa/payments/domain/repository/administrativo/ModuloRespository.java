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
                    JOIN UsuarioClienteEntity uc ON uc.perfil = op.perfil AND uc.cliente.codigo = :idEmpresa
                    JOIN uc.usuario u
                WHERE u.username = :usuario
                  AND o.estadoRegistro = 'S'
                  AND o.opcionPadre IS NOT NULL
                  AND mod2 = m
            )
            """)
    List<ModuloEntity> listModulos(@Param("usuario") String usuario,
                                   @Param("idEmpresa") Long idEmpresa);
}
