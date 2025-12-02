package com.biblioteca.usuario.open.dto;

import com.biblioteca.usuario.internal.entity.Usuario;

import java.time.LocalDate;

public record UsuarioDTO(
        Long idUsuario,
        String nome,
        String email,
        Usuario.TipoUsuario tipoUsuario,
        LocalDate dataCadastro,
        Boolean ativo,
        Boolean bloqueadoPorMultas
) {
    // Record já gera construtor, getters, equals, hashCode e toString
}
