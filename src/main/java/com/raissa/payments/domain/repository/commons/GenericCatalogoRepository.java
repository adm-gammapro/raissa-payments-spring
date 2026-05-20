package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.GenericCatalogoEntity;
import com.raissa.payments.domain.entity.commons.GenericCatalogoPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenericCatalogoRepository extends JpaRepository<GenericCatalogoEntity, GenericCatalogoPK> {
    /**
     * Buscar por tablaConstante
     */
    List<GenericCatalogoEntity> findByIdTablaConstante(String tablaConstante);

    /**
     * Buscar por tablaConstante y campoConstante
     */
    List<GenericCatalogoEntity> findByIdTablaConstanteAndIdCampoConstante(String tablaConstante,
                                                                          String campoConstante);

    /**
     * Buscar por tablaConstante, campoConstante y valorConstante
     */
    Optional<GenericCatalogoEntity> findByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(String tablaConstante,
                                                                                                 String campoConstante,
                                                                                                 String valorConstante);

    /**
     * Buscar por tablaConstante y ordenar por ordenVisualizacion
     */
    List<GenericCatalogoEntity> findByIdTablaConstanteOrderByOrdenVisualizacionAsc(String tablaConstante);

    /**
     * Verificar si existe una combinación específica
     */
    boolean existsByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(String tablaConstante,
                                                                           String campoConstante,
                                                                           String valorConstante);
}
