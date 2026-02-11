package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
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

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seguridad_usuario", schema = "public")
public class UsuarioEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
    @SequenceGenerator(name = "usuario_generator", sequenceName = "public.seguridad_usuario_n_codusu_seq", allocationSize = 1)
    @Column(name = "n_codusu", nullable = false)
    private Long id;

    @Column(name = "c_codusu", nullable = false)
    private String username;

    @Column(name = "c_nombre", nullable = false)
    private String nombres;

    @Column(name = "c_apepat", nullable = false)
    private String apePaterno;

    @Column(name = "c_apemat", nullable = false)
    private String apeMaterno;

    @Column(name = "c_clausu", nullable = false)
    private String password;

    @Column(name = "d_camcla", nullable = false)
    private LocalDate fechaCambioClave;

    @Column(name = "c_inexcl", nullable = false)
    private String indicadorExpiracion;

    @Column(name = "d_expcla", nullable = false)
    private LocalDate fechaExpiracionClave;

    @Column(name = "c_correo", nullable = false)
    private String correo;

    @Column(name = "c_telefo", nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name="c_tipdoc", referencedColumnName = "c_tipdoc", nullable = false)
    private TipoDocumentoEntity tipoDocumento;

    @Column(name = "C_TIPUSU", nullable = false)
    private String tipoUsuario;

    @Column(name = "c_numdoc", nullable = false)
    private String numeroDocumento;

    @Column(name = "b_expira", nullable = false)
    private boolean expired;

    @Column(name = "b_locked", nullable = false)
    private boolean locked;

    @Column(name = "b_creexp", nullable = false)
    private boolean credentialsExpired;

    @Column(name = "b_disabl", nullable = false)
    private boolean disabled;

    @Column(name = "c_claseu", nullable = false)
    private String claseUsuario;
}
