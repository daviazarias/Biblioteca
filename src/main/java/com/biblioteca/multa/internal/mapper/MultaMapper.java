package com.biblioteca.multa.internal.mapper;

import com.biblioteca.multa.internal.entity.Multa;
import com.biblioteca.multa.open.dto.MultaDTO;
import org.springframework.stereotype.Component;

@Component
public class MultaMapper {

    public MultaDTO toDTO(Multa multa) {
        if (multa == null) return null;

        return new MultaDTO(
                multa.getIdMulta(),
                multa.getUsuario().getIdUsuario(),
                multa.getUsuario().getNome(),
                multa.getEmprestimo() != null ? multa.getEmprestimo().getIdEmprestimo() : null,
                multa.getValor(),
                multa.getDataGeracao(),
                multa.getDataPagamento(),
                multa.getStatus(),
                multa.getObservacao()
        );
    }
}
