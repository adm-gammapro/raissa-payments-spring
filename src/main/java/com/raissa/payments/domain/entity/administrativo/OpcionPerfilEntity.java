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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seguridad_opcion_menu_perfil", schema = "public")
public class OpcionPerfilEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opcion_perfil_generator")
    @SequenceGenerator(name = "opcion_perfil_generator", sequenceName = "public.seguridad_opcion_menu_perfil_n_coopme_seq", allocationSize = 1)
    @Column(name = "n_coopme", nullable = false, precision = 11)
    private Long codigo;
    
    @ManyToOne
    @JoinColumn(name = "n_perfil", referencedColumnName = "n_perfil",nullable = false)
    private PerfilEntity perfil;
    
    @ManyToOne
    @JoinColumn(name = "n_opcmen", referencedColumnName = "n_opcmen",nullable = false)
    private OpcionEntity opcion;
}