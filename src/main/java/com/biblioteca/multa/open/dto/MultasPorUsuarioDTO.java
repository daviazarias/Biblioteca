package com.biblioteca.multa.open.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para representar multas agrupadas por usuário
 */
public record MultasPorUsuarioDTO(
        Long idUsuario,
        String nomeUsuario,
        String email,
        Long quantidadeMultas,
        BigDecimal valorTotalPendente,
        BigDecimal valorTotalPago,
        List<MultaDTO> multas
) {
}

