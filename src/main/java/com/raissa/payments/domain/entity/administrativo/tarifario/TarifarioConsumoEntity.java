package com.raissa.payments.domain.entity.administrativo.tarifario;

import com.raissa.comun.general.entity.Auditoria;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
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
import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tarifario_consumo", schema = "public")
public class TarifarioConsumoEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "consumo_generator")
    @SequenceGenerator(
            name = "consumo_generator",
            sequenceName = "public.tarifario_consumo_consumo_id_seq",
            allocationSize = 1)
    private Long consumoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id",
            referencedColumnName = "periodo_id",
            nullable = false)
    private TarifarioPeriodoEntity periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codcli",
            referencedColumnName = "n_codcli",
            nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codusu",
            referencedColumnName = "n_codusu",
            nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id",
            referencedColumnName = "api_id",
            nullable = false)
    private TarifarioApiEntity api;

    @Column(name = "fecha_consumo", nullable = false)
    private LocalDateTime fechaConsumo;

    @Column(name = "codigo_resultado")
    private String codigoResultado;

    @Column(name = "exitoso")
    private Boolean exitoso;

    @Column(name = "facturable")
    private Boolean facturable;

    @Column(name = "importe")
    private BigDecimal importe;

    @Column(name = "observacion")
    private String observacion;
}
