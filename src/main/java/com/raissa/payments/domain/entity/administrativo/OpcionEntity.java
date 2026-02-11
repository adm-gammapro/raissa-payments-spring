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
@Table(name = "seguridad_opcion_menu", schema = "public")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_generator")
    @SequenceGenerator(name = "menu_generator", sequenceName = "public.seguridad_opcion_menu_n_opcmen_seq", allocationSize = 1)
    @Column(name = "n_opcmen", nullable = false, precision = 11, scale = 0)
    private Long codigo;

    @Column(name = "c_desmen", nullable = false, length = 100)
    private String descripcionOpcion;

    @Column(name = "c_rutmen", nullable = false, length = 100)
    private String rutaOpcion;

    @Column(name = "c_parfij", nullable = false, length = 30)
    private String parteFija;

    @Column(name = "c_icomen", nullable = false, length = 1000)
    private String icono;

    @Column(name = "n_opcpad", nullable = false, precision = 11, scale = 0)
    private Long opcionPadre;

    @Column(name = "n_numord", nullable = false, precision = 11, scale = 0)
    private Integer numeroOrden;

    @ManyToOne
    @JoinColumn(name="n_codmod", referencedColumnName = "n_codmod", nullable = false)
    private ModuloEntity modulo;

    @Column(name = "C_OPCSEL", nullable = false, length = 1)
    private String seleccionable;
}