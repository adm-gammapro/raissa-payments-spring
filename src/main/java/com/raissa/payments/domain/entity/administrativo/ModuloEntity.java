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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "generic_modulo", schema = "public")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ModuloEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modulo_generator")
    @SequenceGenerator(name = "modulo_generator", sequenceName = "public.generic_modulo_n_codmod_seq", allocationSize = 1)
    @Column(name = "n_codmod", nullable = false, precision = 11, scale = 0)
    private Long codigo;

    @Column(name = "c_nommod", nullable = false,  length = 50)
    private String nombreModulo;

    @Column(name = "c_desmod", nullable = false, length = 250)
    private String descripcion;

    @Column(name = "c_subtit", length = 250)
    private String subtitulo;

    @Column(name = "c_icomod", length = 1000)
    private String icono;
}
