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
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seguridad_usuario_sistema", schema = "public")
public class UsuarioSistemaEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_sistema_generator")
    @SequenceGenerator(name = "usuario_sistema_generator", sequenceName = "public.seguridad_usuario_sistema_n_ususis_seq", allocationSize = 1)
    @Column(name = "n_ususis")
    private Long codigo;

    @ManyToOne
    @JoinColumn(name = "c_idsist", referencedColumnName = "c_idsist",nullable = false)
    private SistemaEntity sistema;

    @ManyToOne
    @JoinColumn(name = "n_codusu", referencedColumnName = "n_codusu",nullable = false)
    private UsuarioEntity usuario;
}
