package com.biblioteca.reserva.open.dto;

import jakarta.validation.constraints.NotNull;

public record ReservaCreateDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        Long idUsuario,

        @NotNull(message = "ID do livro é obrigatório")
        Long idLivro
) {
}
