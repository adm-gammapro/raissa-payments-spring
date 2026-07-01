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
@Table(name = "tarifario_periodo", schema = "public")
public class TarifarioPeriodoEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "periodo_generator")
    @SequenceGenerator(name = "periodo_generator",
            sequenceName = "public.tarifario_periodo_periodo_id_seq",
            allocationSize = 1)
    @Column(name = "periodo_id")
    private Long periodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suscripcion_id",
            referencedColumnName = "suscripcion_id",
            nullable = false)
    private TarifarioSuscripcionEntity suscripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "limite_usuarios")
    private Integer limiteUsuarios;

    @Column(name = "limite_consumos")
    private Integer limiteConsumos;

    @Column(name = "usuarios_utilizados")
    private Integer usuariosUtilizados;

    @Column(name = "consumos_facturables")
    private Integer consumosFacturables;

    @Column(name = "consumos_no_facturables")
    private Integer consumosNoFacturables;

    @Column(name = "monto_base")
    private BigDecimal montoBase;

    @Column(name = "monto_adicionales")
    private BigDecimal montoAdicionales;

    @Column(name = "monto_consumos")
    private BigDecimal montoConsumos;

    @Column(name = "monto_total")
    private BigDecimal montoTotal;

    @Column(name = "cerrado")
    private Boolean cerrado;
}