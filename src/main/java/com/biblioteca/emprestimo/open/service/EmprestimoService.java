package com.biblioteca.emprestimo.open.service;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import com.biblioteca.emprestimo.internal.mapper.EmprestimoMapper;
import com.biblioteca.emprestimo.internal.repository.EmprestimoRepository;
import com.biblioteca.emprestimo.open.dto.EmprestimoCreateDTO;
import com.biblioteca.emprestimo.open.dto.EmprestimoDTO;
import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.livro.internal.repository.LivroRepository;
import com.biblioteca.multa.open.service.MultaCalculoService;
import com.biblioteca.reserva.internal.entity.Reserva;
import com.biblioteca.reserva.internal.repository.ReservaRepository;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final ReservaRepository reservaRepository;
    private final MultaCalculoService multaCalculoService;
    private final EmprestimoMapper emprestimoMapper;

    public EmprestimoDTO criarEmprestimo(EmprestimoCreateDTO dto) {
        // Regra de negócio: verificar se usuário existe e está ativo
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo não pode realizar empréstimos");
        }

        // Regra de negócio: verificar se livro existe e está disponível
        Livro livro = livroRepository.findById(dto.idLivro())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalStateException("Livro não disponível para empréstimo");
        }

        // Regra de negócio: verificar se há reservas ativas para este livro
        List<Reserva> reservasAtivas = reservaRepository.findReservasAtivasByLivro(livro.getIdLivro());

        if (!reservasAtivas.isEmpty() && livro.getQuantidadeDisponivel() <= 1) {
            // Se há reservas ativas e restaria 0 exemplares após este empréstimo,
            // verificar se o usuário atual tem uma reserva ativa para este livro
            boolean usuarioTemReserva = reservasAtivas.stream()
                    .anyMatch(r -> r.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()));

            if (!usuarioTemReserva) {
                throw new IllegalStateException(
                        "Não é possível realizar o empréstimo. Este livro possui " + reservasAtivas.size() +
                                " reserva(s) ativa(s) e não há exemplares disponíveis suficientes. " +
                                "Reserve o livro primeiro ou aguarde a disponibilidade."
                );
            }
        }

        // Regra de negócio: verificar se usuário tem multas pendentes
        if (multaCalculoService.usuarioTemMultasPendentes(usuario.getIdUsuario())) {
            BigDecimal totalMultas = multaCalculoService.calcularTotalMultasPendentes(usuario.getIdUsuario());
            throw new IllegalStateException(
                    String.format("Usuário possui multas pendentes no valor de R$ %.2f. " +
                                    "Regularize sua situação para realizar novos empréstimos.",
                            totalMultas)
            );
        }

        // Regra de negócio: verificar se usuário já tem empréstimos atrasados
        List<Emprestimo> emprestimosAtrasados = emprestimoRepository
                .findByStatusAndUsuario(Emprestimo.StatusEmprestimo.ATRASADO, usuario.getIdUsuario());

        if (!emprestimosAtrasados.isEmpty()) {
            throw new IllegalStateException("Usuário possui empréstimos atrasados");
        }

        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(dto.dataDevolucaoPrevista());
        emprestimo.setStatus(Emprestimo.StatusEmprestimo.ATIVO);

        // Regra de negócio: decrementar quantidade disponível
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return emprestimoMapper.toDTO(salvo);
    }

    public EmprestimoDTO devolverLivro(Long idEmprestimo) {
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));

        // Regra de negócio: verificar se já foi devolvido
        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Livro já foi devolvido");
        }

        // Registrar devolução
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimo.setStatus(Emprestimo.StatusEmprestimo.DEVOLVIDO);

        // Regra de negócio: incrementar quantidade disponível
        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        Emprestimo atualizado = emprestimoRepository.save(emprestimo);
        return emprestimoMapper.toDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarTodos() {
        List<EmprestimoDTO> list = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findAll()) {
            EmprestimoDTO dto = emprestimoMapper.toDTO(emprestimo);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarAtivos() {
        List<EmprestimoDTO> list = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.ATIVO)) {
            EmprestimoDTO dto = emprestimoMapper.toDTO(emprestimo);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarAtrasos() {
        List<EmprestimoDTO> list = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.ATRASADO)) {
            EmprestimoDTO dto = emprestimoMapper.toDTO(emprestimo);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarPorUsuario(Long idUsuario) {
        List<EmprestimoDTO> list = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findByUsuarioIdUsuario(idUsuario)) {
            EmprestimoDTO dto = emprestimoMapper.toDTO(emprestimo);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<Object[]> obterEmprestimosAtrasados() {
        return emprestimoRepository.findEmprestimosAtrasados();
    }

    @Transactional(readOnly = true)
    public EmprestimoDTO buscarPorId(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));
        return emprestimoMapper.toDTO(emprestimo);
    }

    public void atualizarStatusAtrasados() {
        // Regra de negócio: atualizar status de empréstimos atrasados
        List<Emprestimo> ativos = emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.ATIVO);
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emprestimo : ativos) {
            if (hoje.isAfter(emprestimo.getDataDevolucaoPrevista())) {
                emprestimo.setStatus(Emprestimo.StatusEmprestimo.ATRASADO);
                emprestimoRepository.save(emprestimo);
            }
        }
    }
}
