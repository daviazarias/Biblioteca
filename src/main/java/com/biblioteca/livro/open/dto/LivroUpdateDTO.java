package com.biblioteca.livro.open.dto;

import java.util.Set;

public record LivroUpdateDTO(
        String titulo,
        String autor,
        Integer anoPublicacao,
        Integer quantidadeDisponivel,
        Set<Long> idsCategorias
) {
}
