package com.biblioteca.livro.open.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record LivroCreateDTO(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 200)
        String titulo,

        @NotBlank(message = "Autor é obrigatório")
        @Size(max = 100)
        String autor,

        @NotBlank(message = "ISBN é obrigatório")
        @Pattern(regexp = "^[0-9-]+$", message = "ISBN inválido")
        String isbn,

        @NotNull(message = "Ano de publicação é obrigatório")
        @Min(value = 1000)
        @Max(value = 2100)
        Integer anoPublicacao,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 0)
        Integer quantidadeDisponivel,

        Set<Long> idsCategorias
) {
}
