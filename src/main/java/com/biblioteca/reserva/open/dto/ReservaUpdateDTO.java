package com.biblioteca.reserva.open.dto;

import com.biblioteca.reserva.internal.entity.Reserva;
import jakarta.validation.constraints.NotNull;

public record ReservaUpdateDTO(
        @NotNull(message = "ID da reserva é obrigatório")
        Long idReserva,

        @NotNull(message = "Status é obrigatório")
        Reserva.StatusReserva status
) {
}
