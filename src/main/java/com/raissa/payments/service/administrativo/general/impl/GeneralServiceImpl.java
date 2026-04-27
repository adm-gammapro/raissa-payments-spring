package com.raissa.payments.service.administrativo.general.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import com.raissa.payments.domain.mappers.commons.InstitucionFinancieraMapper;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.commons.InstitucionFinancieraRepository;
import com.raissa.payments.service.administrativo.general.GeneralService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import com.raissa.payments.util.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralServiceImpl extends AbstractRaissaPaymentsService implements GeneralService {
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    //Repositorios
    private final InstitucionFinancieraRepository institucionFinancieraRepository;

    //Mappers
    private final InstitucionFinancieraMapper institucionFinancieraMapper;

    public List<InstitucionFinancieraResponseDto> listarInstitucionFinanciera() {
        List<InstitucionFinancieraEntity> listInstitucionFinanciera = institucionFinancieraRepository.findAll();

        return listInstitucionFinanciera.stream()
                .map(institucionFinancieraMapper::entityToResponseDto)
                .toList();
    }

    public String obtenerCodigoRandom(String username, String passwordPlano, String modoEnvio) {
        Optional<UsuarioEntity> optUser = usuarioRepository.findByUsernameAndEstadoRegistro(username, Constante.ESTADO_ACTIVO);

        if (optUser.isEmpty() || passwordPlano.isEmpty()) {
            return "";
        }

        UsuarioEntity user = optUser.get();

        if(passwordEncoder.matches(passwordPlano, user.getPassword())) {
            String codigo = generarCodigo(6);
            if (modoEnvio.equals("email")) {
                emailService.enviarCodigo("anunez@gammapro.pe", codigo);
                return codigo;
            }

            return "-";
        } else {
            return "";
        }
    }
}