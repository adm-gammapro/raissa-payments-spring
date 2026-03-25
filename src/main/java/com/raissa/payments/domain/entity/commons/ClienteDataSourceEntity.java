package com.raissa.payments.domain.entity.commons;

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

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "raissa_base_datos_cliente", schema = "public")
public class ClienteDataSourceEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_datasource_generator")
    @SequenceGenerator(name = "cliente_datasource_generator", sequenceName = "public.raissa_base_datos_cliente_n_bdclie_seq", allocationSize = 1)
    @Column(name = "n_bdclie", nullable = false)
    private Long codigo;

    @Column(name = "c_coddts", nullable = false)
    private String codigoDataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codcli", referencedColumnName ="n_codcli", nullable = false)
    private ClienteEntity cliente;
}
