package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.OpcionPerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpcionPerfilRepository extends JpaRepository<OpcionPerfilEntity, Long> {
    List<OpcionPerfilEntity> findByPerfilCodigoAndEstadoRegistro(Long perfilId, String estadoRegistro);

    Optional<OpcionPerfilEntity> findByPerfilCodigoAndOpcionCodigoAndEstadoRegistro(
            Long perfilId, Long opcionId, String estadoRegistro);

    boolean existsByPerfilCodigoAndOpcionCodigoAndEstadoRegistro(
            Long perfilId, Long opcionId, String estadoRegistro);

    @Modifying
    @Query("UPDATE OpcionPerfilEntity op SET op.estadoRegistro = 'N' " +
            "WHERE op.perfil.codigo = :perfilId AND op.opcion.codigo IN :opcionesIds AND op.estadoRegistro = 'S'")
    int desasignarOpciones(@Param("perfilId") Long perfilId,
                           @Param("opcionesIds") List<Long> opcionesIds);
}