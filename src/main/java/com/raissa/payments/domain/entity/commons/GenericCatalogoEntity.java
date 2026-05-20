package com.raissa.payments.domain.entity.commons;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "generic_catalogo_constraint", schema = "public")
public class GenericCatalogoEntity extends Auditoria {
    @EmbeddedId
    private GenericCatalogoPK id;

    @Column(name = "c_descri", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "c_abrevi", length = 250)
    private String abreviatura;

    @Column(name = "n_ordvis")
    private Short ordenVisualizacion;
}