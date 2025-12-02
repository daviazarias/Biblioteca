package com.biblioteca.multa.open.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MultaCreateDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        Long idUsuario,

        Long idEmprestimo,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        String observacao
) {
}
