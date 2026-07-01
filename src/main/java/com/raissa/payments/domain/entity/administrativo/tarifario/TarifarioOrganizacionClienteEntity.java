package com.raissa.payments.domain.entity.administrativo.tarifario;

import com.raissa.comun.general.entity.Auditoria;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
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

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tarifario_organizacion_cliente", schema = "public")
public class TarifarioOrganizacionClienteEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tarifario_organizacion_cliente_generator")
    @SequenceGenerator(name = "tarifario_organizacion_cliente_generator",
            sequenceName = "public.tarifario_organizacion_cliente_organizacion_cliente_id_seq",
            allocationSize = 1)
    @Column(name = "organizacion_cliente_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id",
            referencedColumnName = "organizacion_id",
            nullable = false)
    private TarifarioOrganizacionEntity organizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codcli",
            referencedColumnName = "n_codcli",
            nullable = false)
    private ClienteEntity cliente;
}