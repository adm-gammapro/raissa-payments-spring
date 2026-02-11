package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seguridad_perfil", schema = "public")
public class PerfilEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_generator")
    @SequenceGenerator(name = "perfil_generator", sequenceName = "public.seguridad_perfil_n_perfil_seq", allocationSize = 1)
    @Column(name = "n_perfil", nullable = false, precision = 11)
    private Long codigo;

    @Column(name = "c_desper", nullable = false)
    private String descripcion;

    @Column(name = "c_abrper", nullable = false)
    private String abreviatura;

    @Column(name = "c_percom", nullable = false)
    private String nombreComercial;

    @Column(name = "d_feccad", nullable = false)
    private LocalDate fechaCaducidad;
}