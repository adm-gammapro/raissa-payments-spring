package com.raissa.payments.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seguridad_usuario_perfil", schema = "public")
public class UsuarioPerfilEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_usuper")
    private Long codigo;
    
    @ManyToOne
    @JoinColumn(name = "n_perfil", referencedColumnName = "n_perfil",nullable = false)
    private PerfilEntity perfil;
    
    @ManyToOne
    @JoinColumn(name = "n_codusu", referencedColumnName = "n_codusu",nullable = false)
    private UsuarioEntity usuario;
}