package com.biblioteca.config;

import com.biblioteca.usuario.internal.entity.Usuario;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(CustomUserDetailsService.class);
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        // LOG DE DEBUG
        log.info("===== DEBUG AUTENTICAÇÃO =====");
        log.info("Email: {}", email);
        log.info("Senha no banco: {}", usuario.getSenha());
        log.info("Tipo usuario: {}", usuario.getTipoUsuario());
        log.info("Ativo: {}", usuario.isAtivo());

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name())
                ))
                .build();
    }
}
