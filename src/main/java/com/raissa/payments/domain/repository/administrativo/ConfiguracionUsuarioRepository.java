package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ConfiguracionUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionUsuarioRepository extends JpaRepository<ConfiguracionUsuarioEntity, Long> {
}
