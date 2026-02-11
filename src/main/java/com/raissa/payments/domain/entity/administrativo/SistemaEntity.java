package com.raissa.payments.domain.entity.administrativo;

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
@Table(name = "raissa_sistema", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SistemaEntity extends Auditoria {
    @Id
    @Column(name = "c_idsist", length = 3, nullable = false)
    private String id;

    @Column(name = "c_nomsis", length = 50)
    private String nombre;
}
