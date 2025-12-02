package com.biblioteca.emprestimo.open.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoCreateDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        Long idUsuario,

        @NotNull(message = "ID do livro é obrigatório")
        Long idLivro,

        @NotNull(message = "Data de devolução prevista é obrigatória")
        @Future(message = "Data de devolução deve ser futura")
        LocalDate dataDevolucaoPrevista
) {
}
