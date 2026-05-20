package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "seguridad_configuracion_usuario", schema = "public")
public class ConfiguracionUsuarioEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_codcon")
    private Long idConfiguracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codusu", referencedColumnName = "n_codusu", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_codcli", referencedColumnName = "n_codcli", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_perfil", referencedColumnName = "n_perfil", nullable = false)
    private PerfilEntity perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_idsist", referencedColumnName = "c_idsist", nullable = false)
    private SistemaEntity sistema;

}
