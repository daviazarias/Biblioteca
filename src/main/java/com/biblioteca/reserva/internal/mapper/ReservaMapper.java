package com.biblioteca.reserva.internal.mapper;

import com.biblioteca.reserva.internal.entity.Reserva;
import com.biblioteca.reserva.open.dto.ReservaDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaDTO toDTO(Reserva reserva) {
        if (reserva == null) return null;

        return new ReservaDTO(
                reserva.getIdReserva(),
                reserva.getUsuario().getIdUsuario(),
                reserva.getUsuario().getNome(),
                reserva.getLivro().getIdLivro(),
                reserva.getLivro().getTitulo(),
                reserva.getDataReserva(),
                reserva.getDataValidade(),
                reserva.getDataRetirada(),
                reserva.getStatus()
        );
    }
}
