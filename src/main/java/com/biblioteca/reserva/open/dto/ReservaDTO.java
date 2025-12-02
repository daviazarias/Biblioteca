package com.biblioteca.reserva.open.dto;

import com.biblioteca.reserva.internal.entity.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaDTO(
        Long idReserva,
        Long idUsuario,
        String nomeUsuario,
        Long idLivro,
        String tituloLivro,
        LocalDateTime dataReserva,
        LocalDate dataValidade,
        LocalDateTime dataRetirada,
        Reserva.StatusReserva status
) {
}
