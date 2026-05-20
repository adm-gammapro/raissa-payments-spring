package com.raissa.payments.domain.entity.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GenericCatalogoPK implements Serializable {
    @Column(name = "c_nomtab", length = 30)
    private String tablaConstante;

    @Column(name = "c_nomcam", length = 25)
    private String campoConstante;

    @Column(name = "c_valcon", length = 25)
    private String valorConstante;
}
