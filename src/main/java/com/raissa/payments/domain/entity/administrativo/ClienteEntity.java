package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import com.raissa.payments.domain.entity.commons.TipoClienteEntity;
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
@Table(name = "persona_cliente", schema = "public")
public class ClienteEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_generator")
    @SequenceGenerator(name = "cliente_generator", sequenceName = "public.persona_cliente_n_codcli_seq", allocationSize = 1)
    @Column(name = "N_CODCLI", nullable = false)
    private Long codigo;

    @Column(name = "C_RAZSOC", nullable = false)
    private String razonSocial;

    @Column(name = "C_NUMRUC", nullable = false)
    private String ruc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_TIPCLI", referencedColumnName ="C_TIPCLI", nullable = false)
    private TipoClienteEntity tipoCliente;

    @Column(name = "C_DIRECC", nullable = false)
    private String direccion;

    @Column(name = "C_TELFIJ", nullable = false)
    private String telefonoFijo;

    @Column(name = "C_TELCEL", nullable = false)
    private String telefonoCelular;

    @Column(name = "c_usucli", nullable = false)
    private String usuarioCarga;

    @Column(name = "c_clacli", nullable = false)
    private String claveCarga;
}
