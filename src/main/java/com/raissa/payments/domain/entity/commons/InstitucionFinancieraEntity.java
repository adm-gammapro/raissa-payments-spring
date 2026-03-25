package com.raissa.payments.domain.entity.commons;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "generic_institucion_financiera", schema = "public")
public class InstitucionFinancieraEntity extends Auditoria {
	@Id
    @Column(name = "C_CODIFI", nullable = false)
    private String codigo;

	@Column(name = "C_ABRIFI", nullable = false)
	private String abreviatura;

	@Column(name = "C_NOMIFI", nullable = false)
	private String nombre;
	
	@Column(name = "C_CODSBS")
	private String codigoSbs;

	@Column(name = "C_IMAGES")
	private String imagen;

	@Column(name = "C_VALCOL")
	private String color;
}