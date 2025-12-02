package com.biblioteca.multa.open.dto;

import com.biblioteca.multa.internal.entity.Multa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MultaDTO(
        Long idMulta,
        Long idUsuario,
        String nomeUsuario,
        Long idEmprestimo,
        BigDecimal valor,
        LocalDate dataGeracao,
        LocalDate dataPagamento,
        Multa.StatusMulta status,
        String observacao
) {
}
