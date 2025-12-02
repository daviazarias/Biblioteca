package com.biblioteca.livro.open.dto;

import java.time.LocalDate;
import java.util.Set;

public record LivroDTO(
        Long idLivro,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        LocalDate dataAdicao,
        Integer quantidadeDisponivel,
        Integer quantidadeTotal,
        Integer disponivelParaEmprestimo,
        Integer quantidadeReservada,
        Set<String> categorias
) {
}
