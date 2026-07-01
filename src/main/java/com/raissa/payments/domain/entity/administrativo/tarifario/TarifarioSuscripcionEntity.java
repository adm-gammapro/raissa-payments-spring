package com.raissa.payments.domain.entity.administrativo.tarifario;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tarifario_suscripcion", schema = "public")
public class TarifarioSuscripcionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "suscripcion_generator")
    @SequenceGenerator(name = "suscripcion_generator",
            sequenceName = "public.tarifario_suscripcion_suscripcion_id_seq",
            allocationSize = 1)
    @Column(name = "suscripcion_id", nullable = false)
    private Long suscripcionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id",
            referencedColumnName = "organizacion_id",
            nullable = false)
    private TarifarioOrganizacionEntity organizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id",
            referencedColumnName = "plan_id",
            nullable = false)
    private TarifarioPlanEntity plan;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "cancelada")
    private Boolean cancelada;

    @Column(name = "fecha_cancelacion")
    private LocalDate fechaCancelacion;

    @Column(name = "meses_vigencia")
    private Integer mesesVigencia;

    @Column(name = "precio_contratado")
    private BigDecimal precioContratado;

    @Column(name = "usuarios_contratados")
    private Integer usuariosContratados;

    @Column(name = "consumos_contratados")
    private Integer consumosContratados;

    @Column(name = "periodo_prueba")
    private Boolean periodoPrueba;

    @Column(name = "fecha_inicio_prueba")
    private LocalDate fechaInicioPrueba;

    @Column(name = "fecha_fin_prueba")
    private LocalDate fechaFinPrueba;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;
}