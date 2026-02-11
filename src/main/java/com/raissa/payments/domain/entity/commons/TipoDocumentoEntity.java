package com.raissa.payments.domain.entity.commons;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "generic_tipo_documento", schema = "public")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoEntity extends Auditoria {
    @Id
    @Column(name = "c_tipdoc", nullable = false, length = 2)
    private String codigo;

    @Column(name = "c_abrdoc", nullable = false, length = 10)
    private String abreviatura;

    @Column(name = "c_descri", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "n_lenmin", nullable = false, precision = 11, scale = 0)
    private Integer longitudMinima;

    @Column(name = "n_lenmax", nullable = false, precision = 11, scale = 0)
    private Integer longitudMaxima;

    @Column(name = "c_inpeju", nullable = false, length = 1)
    private String indicadorPersonaJuridica;

    @Column(name = "c_inpena", nullable = false, length = 1)
    private String indicadorPersonaNatural;

    @Column(name = "c_incona", nullable = false, length = 1)
    private String documentoComplementario;

    @Column(name = "c_equsbs", nullable = false, length = 3)
    private String equivalenteSbs;

    @Column(name = "c_equsen", nullable = false, length = 2)
    private String equivalenteSentinel;
}