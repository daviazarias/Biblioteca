package com.biblioteca.usuario.internal.mapper;

import com.biblioteca.usuario.internal.entity.Usuario;
import com.biblioteca.usuario.open.dto.UsuarioCreateDTO;
import com.biblioteca.usuario.open.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getDataCadastro(),
                usuario.isAtivo(),
                Boolean.FALSE
        );
    }

    public Usuario toEntity(UsuarioCreateDTO dto, String senhaEncriptada) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(senhaEncriptada);
        usuario.setTipoUsuario(dto.tipoUsuario());
        usuario.setDataCadastro(LocalDate.now());
        usuario.setAtivo(true);
        return usuario;
    }
}
