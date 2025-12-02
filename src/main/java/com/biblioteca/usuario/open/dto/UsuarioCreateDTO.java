package com.biblioteca.usuario.open.dto;

import com.biblioteca.usuario.internal.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100)
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6)
        String senha,

        @NotNull(message = "Tipo de usuário é obrigatório")
        Usuario.TipoUsuario tipoUsuario
) {
}
