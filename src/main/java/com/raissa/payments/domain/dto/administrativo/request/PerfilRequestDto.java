package com.raissa.payments.domain.dto.administrativo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Valid
public class PerfilRequestDto {
    private Long codigo;

    @NotNull(message = "La descripción no puede ser nula")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\-_,.]+$",
            message = "La descripción contiene caracteres no permitidos")
    private String descripcion;

    @NotNull(message = "La abreviatura no puede ser nula")
    @Size(max = 20, message = "La abreviatura debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[A-Z0-9_]+$",
            message = "La abreviatura solo puede contener letras mayúsculas, números y guiones bajos")
    private String abreviatura;

    @Size(max = 100, message = "El nombre comercial debe tener entre 3 y 100 caracteres")
    private String nombreComercial;

    private String fechaCaducidad;

    private Long codigoCliente;
}
