package com.biblioteca.usuario.open.service;

import com.biblioteca.multa.open.service.MultaCalculoService;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.biblioteca.usuario.internal.mapper.UsuarioMapper;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import com.biblioteca.usuario.open.dto.UsuarioCreateDTO;
import com.biblioteca.usuario.open.dto.UsuarioDTO;
import com.biblioteca.usuario.open.dto.UsuarioUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    public static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado";
    private static final Logger log = LogManager.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final MultaCalculoService multaCalculoService;

    public UsuarioDTO criarUsuario(UsuarioCreateDTO dto) {
        // Regra de negócio: verificar se email já existe
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }

        // Regra de negócio: encriptar senha
        String senhaEncriptada = passwordEncoder.encode(dto.senha());

        Usuario usuario = usuarioMapper.toEntity(dto, senhaEncriptada);
        Usuario salvo = usuarioRepository.save(usuario);
        UsuarioDTO dtoResp = usuarioMapper.toDTO(salvo);
        boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(dtoResp.idUsuario());
        return new UsuarioDTO(
                dtoResp.idUsuario(), dtoResp.nome(), dtoResp.email(), dtoResp.tipoUsuario(),
                dtoResp.dataCadastro(), dtoResp.ativo(), bloqueado
        );
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        List<UsuarioDTO> list = new ArrayList<>();
        for (Usuario usuario : usuarioRepository.findAll()) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(dto.idUsuario());
            dto = new UsuarioDTO(dto.idUsuario(), dto.nome(), dto.email(), dto.tipoUsuario(), dto.dataCadastro(), dto.ativo(), bloqueado);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarAtivos() {
        List<UsuarioDTO> list = new ArrayList<>();
        for (Usuario usuario : usuarioRepository.findByAtivoTrue()) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(dto.idUsuario());
            dto = new UsuarioDTO(dto.idUsuario(), dto.nome(), dto.email(), dto.tipoUsuario(), dto.dataCadastro(), dto.ativo(), bloqueado);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));
        UsuarioDTO dto = usuarioMapper.toDTO(usuario);
        boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(dto.idUsuario());
        return new UsuarioDTO(dto.idUsuario(), dto.nome(), dto.email(), dto.tipoUsuario(), dto.dataCadastro(), dto.ativo(), bloqueado);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));
        UsuarioDTO dto = usuarioMapper.toDTO(usuario);
        boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(dto.idUsuario());
        return new UsuarioDTO(dto.idUsuario(), dto.nome(), dto.email(), dto.tipoUsuario(), dto.dataCadastro(), dto.ativo(), bloqueado);
    }

    public void inativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        // Regra de negócio: não permitir inativar administradores
        if (usuario.getTipoUsuario() == Usuario.TipoUsuario.ADMIN) {
            throw new IllegalStateException("Não é possível inativar um administrador");
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTipoUsuario(dto.tipoUsuario());
        usuario.setAtivo(dto.ativo());

        usuarioRepository.save(usuario);
        UsuarioDTO resp = usuarioMapper.toDTO(usuario);
        boolean bloqueado = multaCalculoService.usuarioTemMultasPendentes(resp.idUsuario());
        return new UsuarioDTO(resp.idUsuario(), resp.nome(), resp.email(), resp.tipoUsuario(), resp.dataCadastro(), resp.ativo(), bloqueado);
    }

    @Transactional(readOnly = true)
    public List<Object[]> obterUsuariosComMaisEmprestimos(LocalDate dataInicio, LocalDate dataFim) {
        return usuarioRepository.findUsuariosComMaisEmprestimos(dataInicio, dataFim);
    }
}
