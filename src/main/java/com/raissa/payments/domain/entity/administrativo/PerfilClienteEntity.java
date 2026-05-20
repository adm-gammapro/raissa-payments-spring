package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "seguridad_perfil_cliente", schema = "public")
public class PerfilClienteEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_cliente_generator")
    @SequenceGenerator(name = "perfil_cliente_generator", sequenceName = "public.seguridad_perfil_cliente_n_percli_seq", allocationSize = 1)
    @Column(name = "n_percli", nullable = false, precision = 11)
    private Long codigo;
    
    @ManyToOne
    @JoinColumn(name = "n_perfil", referencedColumnName = "n_perfil",nullable = false)
    private PerfilEntity perfil;
    
    @ManyToOne
    @JoinColumn(name = "n_codcli", referencedColumnName = "n_codcli",nullable = false)
    private ClienteEntity cliente;
}