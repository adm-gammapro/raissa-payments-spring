package com.raissa.payments.domain.entity.administrativo.tarifario;

import com.raissa.comun.general.entity.Auditoria;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
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

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tarifario_plan", schema = "public")
public class TarifarioPlanEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tarifario_plan_generator")
    @SequenceGenerator(name = "tarifario_plan_generator",
            sequenceName = "public.tarifario_plan_plan_id_seq",
            allocationSize = 1)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_idsist",
            referencedColumnName = "c_idsist",
            nullable = false)
    private SistemaEntity sistema;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "modalidad")
    private String modalidad;

    @Column(name = "precio_base")
    private BigDecimal precioBase;

    @Column(name = "usuarios_incluidos")
    private Integer usuariosIncluidos;

    @Column(name = "consumos_incluidos")
    private Integer consumosIncluidos;

    @Column(name = "permite_prueba")
    private Boolean permitePrueba;

    @Column(name = "dias_prueba")
    private Integer diasPrueba;

    @Column(name = "cobra_por_consumo")
    private Boolean cobraPorConsumo;
}
