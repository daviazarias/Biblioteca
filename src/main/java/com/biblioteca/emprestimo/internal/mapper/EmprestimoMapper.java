package com.biblioteca.emprestimo.internal.mapper;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import com.biblioteca.emprestimo.open.dto.EmprestimoDTO;
import com.biblioteca.multa.open.service.MultaCalculoService;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoMapper {

    private final MultaCalculoService multaCalculoService;

    public EmprestimoMapper(MultaCalculoService multaCalculoService) {
        this.multaCalculoService = multaCalculoService;
    }

    public EmprestimoDTO toDTO(Emprestimo emprestimo) {
        if (emprestimo == null) {
            return null;
        }
        boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(emprestimo.getUsuario().getIdUsuario());
        return new EmprestimoDTO(
                emprestimo.getIdEmprestimo(),
                emprestimo.getUsuario().getIdUsuario(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getIdLivro(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoPrevista(),
                emprestimo.getDataDevolucaoReal(),
                emprestimo.getStatus(),
                bloqueado
        );
    }
}
