package com.biblioteca.emprestimo.open.dto;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;

import java.time.LocalDate;

public record EmprestimoDTO(
        Long idEmprestimo,
        Long idUsuario,
        String nomeUsuario,
        Long idLivro,
        String tituloLivro,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucaoPrevista,
        LocalDate dataDevolucaoReal,
        Emprestimo.StatusEmprestimo status,
        Boolean usuarioBloqueadoPorMultas
) {
}
