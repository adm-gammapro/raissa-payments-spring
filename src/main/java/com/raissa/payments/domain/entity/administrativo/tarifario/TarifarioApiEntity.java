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
@Table(name = "tarifario_api", schema = "public")
public class TarifarioApiEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tarifario_api_generator")
    @SequenceGenerator(name = "tarifario_api_generator",
            sequenceName = "public.tarifario_api_api_id_seq",
            allocationSize = 1)
    @Column(name = "api_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_idsist",
            referencedColumnName = "c_idsist",
            nullable = false)
    private SistemaEntity sistema;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "facturable", nullable = false)
    private Boolean facturable;

    @Column(name = "tipo_cobro", nullable = false, length = 1)
    private String tipoCobro;

    @Column(name = "precio_unitario", precision = 12, scale = 4)
    private BigDecimal precioUnitario;

}
