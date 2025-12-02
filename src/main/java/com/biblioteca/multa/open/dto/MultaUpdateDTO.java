package com.biblioteca.multa.open.dto;

import com.biblioteca.multa.internal.entity.Multa;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MultaUpdateDTO(
        @NotNull(message = "ID da multa é obrigatório")
        Long idMulta,
        BigDecimal valor,
        @NotNull(message = "Status é obrigatório")
        Multa.StatusMulta status,
        String observacao
) {
}
