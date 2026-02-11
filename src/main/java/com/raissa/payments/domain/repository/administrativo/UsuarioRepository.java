package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsernameAndEstadoRegistro(String username, String estadoRegistro);
}
