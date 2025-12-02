package com.biblioteca.usuario.open.dto;

import com.biblioteca.usuario.internal.entity.Usuario;

public record UsuarioUpdateDTO(
        String nome,
        String email,
        Usuario.TipoUsuario tipoUsuario,
        Boolean ativo
) {
}
